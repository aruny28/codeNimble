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
        self.driver.get('https://m-dev2.kohlsecommerce.com/')
        self.driver.add_cookie({'name': 'kohls_debug', 'value': 'true'})
        time.sleep(0.5)
        
        
        self.driver.find_element_by_xpath('//*[@id="mcom-header"]/div[2]/div[2]/div[2]').click()
        
        time.sleep(2)
        self.driver.find_element_by_id('loginEmail').send_keys('eliteuser16@loy.com')
        self.driver.find_element_by_id('loginPassword').send_keys('Test@123')
        
        self.driver.find_element_by_id('sign-in-btn').click()

        time.sleep(10)
        #self.driver.switch_to.context(self.driver.contexts[0])
        #time.sleep(2)
        #try:
        #    self.driver.find_element_by_id('reward-modal-close').click()
        #except:
        #    pass
        #time.sleep(5)
        #try:
        #    self.driver.find_element_by_xpath('//*[@id="loyaltyPilotWelcomeOverlay"]/div/div/div[1]/div/div[1]').click()
        #except:
        #    pass
        
        #time.sleep(5)
        #self.driver.switch_to.context(self.driver.contexts[1])
        
        time.sleep(10)
        self.driver.find_element_by_class_name('menu-icon').click()
        
        
        print 'Scenario.begin walet'
        self.driver.find_element_by_class_name('hb-wallet').click()
        kohlscash = self.driver.find_element_by_class_name('acc_green_num pilot-green kc-amount-position ng-binding')
        time.sleep(2)
        kohlscash.click()
        self.driver.find_element_by_class_name('offer_status   ng-binding offer_avail')

        
        print 'Scenario.end walet'
    	time.sleep(10)
        self.driver.quit()
