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

    def test_additem(self):
        time.sleep(2)
        self.driver.get('https://m-dev2.kohlsecommerce.com/product/prd-1476171/reduce-waterweek-cardigan-6-pc-water-bottle-set.jsp?prdPV=1&userPFM=water%20bottles&diestoreid=217&selectShip=true')
        self.driver.add_cookie({'name': 'kohls_debug', 'value': 'true'})
        time.sleep(0.5)
        self.driver.find_element_by_id('zoom-img-0') #make sure page loaded
        #self.driver.execute_script("mobile: scroll", {"direction": "up"})
        self.driver.find_element_by_id('add-to-bag-btn').click()
        self.driver.find_element_by_class_name('close-btn').click()
        self.driver.find_element_by_class_name('mcom-cart-icon').click()
        self.driver.find_element_by_class_name('btn-move-to-checkout').click()
        self.driver.find_element_by_id('loginEmail').send_keys('eliteuser16@loy.com')
        self.driver.find_element_by_id('loginPassword').send_keys('Test@123')
        self.driver.find_element_by_id('sign-in-btn').click()
        time.sleep(10)
        self.driver.switch_to.context(self.driver.contexts[0])
        time.sleep(5)
        self.driver.page_source
        time.sleep(5)
        try:
            self.driver.find_element_by_class_name('android.widget.Image').click()
        except:
            pass
        try:
            self.driver.find_element_by_id('reward-modal-close').click()
        except:
            pass
        self.driver.switch_to.context(self.driver.contexts[1])
        time.sleep(5)
        self.driver.page_source
        #time.sleep(5)
        #self.driver.find_element_by_link_text('Remove')
        print 'Scenario.begin checkout'
        self.driver.find_element_by_class_name('btn-move-to-checkout').click()
        self.driver.find_element_by_class_name('order-confirm-title')
        print 'Scenario.end checkout'
    	time.sleep(10)
        self.driver.quit()
