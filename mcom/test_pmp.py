import json
import os
import time
import unittest
from appium import webdriver

class TestKohlsWeb(unittest.TestCase):

    def setUp(self):
        self.driver = webdriver.Remote(
            command_executor = os.getenv('COMMAND_EXECUTOR'),
            desired_capabilities = json.loads(os.getenv('DESIRED_CAPABILITIES'))
        )
        self.driver.implicitly_wait(5)

    def test_pmp(self):
        time.sleep(2)
        print 'Scenario.begin load_products'
        self.driver.get('https://m-dev2.kohlsecommerce.com/search.jsp?search=shirts&isFromSearch=true&defaultStoreId=725')
        self.driver.add_cookie({'name': 'kohls_debug', 'value': 'true'})

        # Make sure filters have been applied and page has loaded
        #self.driver.find_element_by_id('active-dimensions');
        self.driver.find_elements_by_class_name('sale-price')
        print 'Scenario.end load_products'
	
    	time.sleep(10)
        self.driver.quit()
