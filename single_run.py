#!/usr/bin/env python3\r\n
import os
import yaml
import json
from collections import namedtuple
import math
import time
import time as timer
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, ElementNotVisibleException, ElementNotSelectableException, NoSuchElementException
import selenium.webdriver.support.ui as ui


######################################################################################################################
def read_yml():
    dir_path = os.path.dirname(os.path.abspath(__file__))
    file = open(dir_path+"/config.yml", 'r')
    yaml_obj = yaml.load(file, Loader=yaml.SafeLoader)
    json_string = json.dumps(yaml_obj)
    config_obj = json.loads(json_string, object_hook=lambda d: namedtuple(
        'config', d.keys())(*d.values()))
    return config_obj


configuration = read_yml()
######################################################################################################################
import mysql.connector


def connect_to_db():
    return mysql.connector.connect(host=str(configuration.stringHost), user=str(configuration.stringUser),
                                   passwd=str(configuration.stringPass), database=str(configuration.stringDb))


def select_rows(connection):
    query = "select account, broker, server, action, symbol, id from orders where status = 0;"
    # ('xx1', 'xx3', 'xx2', 'buy', 'EURUSD', 10)
    my_cursor = connection.cursor()
    my_cursor.execute(query)
    return my_cursor.fetchall()


def update_order_status(connection, id):
    query = 'UPDATE orders SET status = 0 WHERE id= {}'.format(id)
    my_cursor = connection.cursor()
    my_cursor.execute(query)
    connection.commit()


def close_db(connection):
    connection.close()
######################################################################################################################
retry_ops = False
retry_count = 0
def runCrawler(buy_sell="Buy", min_rate="1", max_rate="1000",
               currency="AUD/USD", time="3am-5am", size="1"):
    global retry_ops, retry_count
    retry_ops = True
    try:
        retry_count =  retry_count + 1
        print('page loading')
        login()
        print('login')
        # print('wait for page load')
        wait_for_page_load()
        # print('closing welcome pop up')
        close_welcome_pop_up()
        # select currency to buy from side panel
        select_currency_to_buy(currency, time)
        # making deal
        retry_ops = not make_deal(min_rate, max_rate, buy_sell, size)
    except Exception as e:
        print('retrying price updated while placing order....')
        if retry_ops and retry_count < 4:
            runCrawler(buy_sell, min_rate, max_rate, currency, time, size)
        elif retry_count == 4:
            print("skipping operation as the price updating very frequently")
######################################################################################################################
def close_welcome_pop_up():
    click_element_by_css(
        'span:nth-child(12) .winConCls')


def close_market_close_pop_up():
    wait_until_element_clickable(
        by_id, 'clsExchangeClosedWindow', catch_exception=True).click()
######################################################################################################################
# followign elements belong to the order screen only for buying and selling
dealings_iframe_locator = '#ifrDealingRates'
dealing_table = '//*[@id="table-1linked"]'
dealings_currency_buy_price_xpath = '{}/tbody/*/td[@class="buyCell"]'.format(
    dealing_table)
dealings_currency_sell_price_xpath = '{}/tbody/*/td[@class="sellCell"]'.format(
    dealing_table)
buy_sell_currency_selector = '{}/tbody/tr[{}]/td[1]'
buy_sell_price_for_selected_lot_price_update = None
buy_sell_price_for_selected_lot_price = '//*[@id="ticket1_tabContainer1_Page1_offerCell"][contains(text(),"{}")]'
bid_price_selected_updated = None
bid_price_selected = '//*[@id="ticket1_tabContainer1_Page1_bidCell"][contains(text(),"{}")]'
buy_sell_size = '//*[@id="ticket1_tabContainer1_Page1_sizeField"]/span/input'
buy_button = '//*[@id="ticket1_tabContainer1_Page1_buybutton"]'
sell_button = '//*[@id="ticket1_tabContainer1_Page1_sellbutton"]'
price_input = '//*[@id="ticket1_tabContainer1_Page1_priceField"]/span/input'
lot_size_input = '//*[@id="ticket1_tabContainer1_Page1_sizeField"]/span/input'
place_order_button = '//*[@id="ticket1_orderTicketButton"]'
order_confirmation_message = '//*[@id="ticket1_OrderReceivedTitle"][contains(text(), "Order Received")]'
order_summary_table = '//*[@id="ticket1_OrderReceivedSummary"]/tbody'
close_order_summary = '//*[@id="ticket1_OrderReceivedClose"]'


def make_deal(min_rate, max_rate, buy_or_sell, size):
    switch_to_dealing_frame()
    index, price = buy_sell_currency_price(min_rate, max_rate, buy_or_sell)
    if index >= 0:
        return complete_transaction(buy_or_sell, size, price)
    return False


def switch_to_dealing_frame():
    default_content()
    switch_to_iframe(dealings_iframe_locator)


def buy_sell_currency_price(min_rate, max_rate, buy_or_sell='buy'):
    wait_until_element_clickable(by_xpath, dealings_currency_sell_price_xpath)
    if buy_or_sell == 'buy':
        return buy_currency(min_rate, max_rate)
    else:
        return sell_currency(min_rate, max_rate)


def buy_currency(min_rate, max_rate):
    buy_prices_ele = driver.find_elements(
        by_xpath, dealings_currency_buy_price_xpath)
    return select_buy_rate(min_rate, max_rate, buy_prices_ele)


def sell_currency(min_rate, max_rate):
    sell_prices_ele = driver.find_elements(
        by_xpath, dealings_currency_sell_price_xpath)
    return select_sell_rate(min_rate, max_rate, sell_prices_ele)


def select_buy_rate(min_range, max_range, price_list_ele):
    min_price = math.inf
    min_price_index = -1
    i = 0
    price = 0
    for ele in price_list_ele:
        i = i + 1
        price_text = ele.text
        # print('price : '+ price_text)
        # print ('price to check')
        if price_text!= '-':
            price = float(price_text)
        else:
            continue
        if float(min_range) < price < float(max_range) and min_price >= price:
            min_price = price
            min_price_index = i
    # print ("min_price_index   :" + str(min_price_index ) )
    print("buy_price   :" + str(min_price))
    global buy_sell_price_for_selected_lot_price_update
    buy_sell_price_for_selected_lot_price_update = buy_sell_price_for_selected_lot_price.format(
        min_price)
    click_element_by_xpath(buy_sell_currency_selector.format(
        dealing_table, min_price_index))
    return min_price_index, min_price


def select_sell_rate(min_range, max_range, price_list_ele):
    max_price = -math.inf
    max_price_index = -1
    i = 0
    price = 0
    for ele in price_list_ele:
        i = i + 1
        price_text = ele.text
        # print('price : ' + price_text)
        # print('price to check')
        if price_text != '-':
            price = float(price_text)
        else:
            continue
        if float(min_range) < price < float(max_range) and max_price <= price:
            max_price = price
            max_price_index = i
    # print("deal_price_index   :" + str(max_price_index))
    print("deal price   :" + str(max_price))
    global bid_price_selected_updated
    bid_price_selected_updated = bid_price_selected.format(max_price)
    # print("selector  ... "+buy_sell_currency_selector.format(
    #     dealing_table, max_price_index))
    click_element_by_xpath(buy_sell_currency_selector.format(
        dealing_table, max_price_index))
    return max_price_index, max_price


def complete_transaction(buy_sell, size, price):
    switch_to_iframe('#ifrBetslipHolder')
    # waiting for order window loading
    # print('lot price dispayed ')
    if buy_sell == 'buy':
        wait_until_element_clickable(by_xpath, buy_sell_price_for_selected_lot_price_update)
        print('buying.. ')
        find_element_and_click_by_xpath(buy_button)
    else:
        wait_until_element_clickable(by_xpath, bid_price_selected_updated)
        print('selling.. ')
        find_element_and_click_by_xpath(sell_button)
    print('lot size entered ...' + str(size))
    find_element_and_send_value_by_xpath(lot_size_input, size)
    print('lot price entered ...' + str(price))
    find_element_and_send_value_by_xpath(price_input, str(price))
    click_element_by_xpath(place_order_button)
    wait_for_order_confirmation()
    print('placed order..')
    return True


def wait_for_order_confirmation():
    wait_until_element_clickable(by_xpath, order_confirmation_message)
    time.sleep(2)
    print('order complete')

######################################################################################################################
import platform
# get absolute path for current file
dir_path = os.path.dirname(os.path.abspath(__file__))

# by selectors
by_css = By.CSS_SELECTOR
by_xpath = By.XPATH
by_id = By.ID
def open_chrome_broser(url="https://demo.nadex.com/login"):
    if platform.system() =='Darwin':
        chromedriver = dir_path + "/chromedriver"
    elif platform.system() =='Linux':
        chromedriver = dir_path + "/chromedriver"
    elif platform.system() =='Windows':
        chromedriver = dir_path + "/chromedriver.exe"
    options = Options()
    # options.add_argument('--headless')
    options.add_argument('--disable-gpu')
    wd = webdriver.Chrome(chromedriver, options=options)
    wd.set_page_load_timeout(120)
    wd.maximize_window()
    print('defined new driver')
    return wd


# open browser
driver = open_chrome_broser()


def open_url():
    driver.get(configuration.app_url)


def close_browser():
    driver.quit()


def refresh_browser():
    driver.refresh()


def default_content():
    driver.switch_to.default_content()


def element_visible(locator, timeout=30):
    try:
        ui.WebDriverWait(driver, timeout).until(
            EC.visibility_of_element_located((by_css, locator)))
        return True
    except TimeoutException:
        return False


def switch_to_iframe(locator, time_out=60):
    # wait = WebDriverWait(driver, 60, poll_frequency=5, ignored_exceptions=[
    #                      ElementNotVisibleException, ElementNotSelectableException])
    # i_frame = wait.until(lambda driver: driver.find_element(by_css, locator))
    # i_frame = wait_until_element_clickable(
    #     by_css, locator=locator, catch_exception=False)
    # if i_frame:
    #     driver.switch_to.frame(i_frame)
    default_content()
    if element_visible(locator, time_out):
        driver.switch_to.frame(driver.find_element_by_css_selector(locator))
    else:
        raise Exception('Frame not found..')


def click_element_by_css(css_selector, catch_exception=False):
    # print('clicking by css and wait time...')
    ele = wait_until_element_clickable(
        by_css, locator=css_selector, catch_exception=catch_exception)
    if ele:
        ele.click()


def click_element_by_xpath(xpath, catch_exception=False):
    # print('clicking by xpath ..')
    ele = wait_until_element_clickable(
        by_xpath, locator=xpath, catch_exception=catch_exception)
    if ele:
        ele.click()


def click_element_by_id(element_id):
    # print('clicking by id and wait time...')
    wait_until_element_clickable(by_id, element_id)
    driver.find_element_by_id(element_id).click()


def find_element_and_send_value(element_id, value):
    driver.find_element_by_id(element_id).send_keys(value)


def find_element_and_send_value_by_xpath(element_xpath, value):
    driver.find_element_by_xpath(element_xpath).send_keys(value)


def find_element_and_click_by_xpath(element_xpath):
    driver.find_element_by_xpath(element_xpath).click()


def wait_until_element_clickable(by, locator, catch_exception=False, wait_time=120):
    # print('wait until element clickable ' + locator)
    wait = WebDriverWait(driver, wait_time, poll_frequency=5, ignored_exceptions=[
                         ElementNotVisibleException, ElementNotSelectableException])
    try:
        ele = wait.until(EC.element_to_be_clickable((by, locator)))
        # print('found element with locator ' + locator)
        return ele
    except TimeoutException as e:
        # print("time out Exception...")
        print(e.stacktrace)
        if not catch_exception:
            raise e
        return None


def wait_for_page_load():
    wait = WebDriverWait(driver, 30, poll_frequency=5, ignored_exceptions=[
                         ElementNotVisibleException, ElementNotSelectableException])
    try:
        wait.until(lambda driver: driver.find_element_by_id(
            'btnPrices').is_displayed())
        # print('page loaded in first wait cycle')
    except TimeoutException as e:
        # print("refreshing browser")
        refresh_browser()
        # print('2nd cycle starts....')
        wait.until(lambda driver: driver.find_element_by_id(
            'btnPrices').is_displayed())
        # print('found element in second wait cycle')

######################################################################################################################
logged_in = False


def login():
    global logged_in
    if not logged_in:
        open_url()
        find_element_and_send_value("account_id", configuration.user_name)
        find_element_and_send_value("password",  configuration.password)
        click_element_by_id("loginbutton")
        logged_in = True
        refresh_browser()
        print('login done')
    else:
        print('skipping login ..')
        print('already logged in ..')
        refresh_browser()
######################################################################################################################
sidepanel_iframe_css = '#ifrFinder'
forext_binary = '//*[@id="node158700"]'
currency_tree_head = "{}/..//*[contains(text(), '{}')]"
time_element = '{}/..//*[contains(text(), "{}")]'


def select_currency_to_buy(currency, time):
    switch_to_side_panel()
    expand_forex_binaries()
    select_currency(currency)
    select_time(time)


def switch_to_side_panel():
    default_content()
    switch_to_iframe(sidepanel_iframe_css)
    # print('switched to side panel')


def expand_forex_binaries():
    click_element_by_xpath(forext_binary)


def select_currency(currency):
    click_element_by_xpath(currency_tree_head.format(forext_binary, currency))


def select_time(time):
    click_element_by_xpath(time_element.format(forext_binary, time))

######################################################################################################################
######################################################################################################################
######################################################################################################################
######################################################################################################################
######################################################################################################################
def runner():
    # connect to db
    connection = connect_to_db()
    try:
        # select rows to execute crawler
        result = select_rows(connection)
        # result = [('xx1', 'xx3', 'xx2', 'buy', 'EURUSD', 10),('xx1', 'xx3', 'xx2', 'sell', 'EURUSD', 10)]
        # execute the crawler
        if(len(result)==0):
            print('no records found in db to place the order')
        else:
            for record in result:
                print("================================================================================")
                if (record[0] == configuration.stringMyAccount and record[1] == configuration.stringMyBroker and
                        record[2] == configuration.stringMyServer):
                    print("processing record ... id = " + str(record[5]))
                    runCrawler(buy_sell=record[3], min_rate=configuration.min, max_rate=configuration.max,
                               currency=record[4][:3] + '/' + record[4][3:], time=configuration.time, size=configuration.size)
                    # update order status to DB
                    update_order_status(connection, record[5])
                else:
                    print("skipping record ... id = "+ str(record[5]))
    finally:
        # close db
        close_db(connection)
        driver.quit()

runner()