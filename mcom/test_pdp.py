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
        self.driver.implicitly_wait(10)

    def test_pdp(self):
        time.sleep(5)
        print 'Scenario.begin load_product_details'
        self.driver.get('https://m-dev2.kohlsecommerce.com/product/prd-1997448/iz-byer-california-chiffon-colorblock-top-juniors-plus.jsp?prdPV=3&userPFM=shirts&diestoreid=217&selectShip=true')
        self.driver.add_cookie({'name': 'kohls_debug', 'value': 'true'})

        # Make sure product page has loaded
        add_to_cart = self.driver.find_element_by_id('add-to-bag-btn')
        print 'Scenario.end load_product_details'

    	time.sleep(10)
        self.driver.quit()
