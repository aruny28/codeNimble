import json
import os
import time
import unittest
import traceback
import random
from appium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


class TestKohlsiOS(unittest.TestCase):

    def setUp(self):
        self.driver = webdriver.Remote(
            command_executor = os.getenv('COMMAND_EXECUTOR'),
            desired_capabilities = json.loads(os.getenv('DESIRED_CAPABILITIES'))
        )
        self.driver.implicitly_wait(12)

    def test_shop(self):
        try:
            users = ["eliteuser16@loy.com", "eliteuser6@loy.com", "eliteuser25@loy.com", "eliteuser9@loy.com"]
            # give some time for autoAccept capability to click through all alerts
            time.sleep(10)
            #print 'Scenario.begin load_Home'
            self.driver.find_element_by_accessibility_id('Agree').click()
            # time for alerts
            time.sleep(10)
            #print 'Scenario.end load_Home'


            self.driver.find_element_by_accessibility_id('MENU_OPEN_ID').click()
            time.sleep(8)
            

            try:
                self.driver.find_element_by_accessibility_id('Not Now').click()
            except:
                pass

            self.driver.find_element_by_accessibility_id('buttonSignin').click()
            i = random.randint(0,3)
            self.driver.find_element_by_accessibility_id('textFieldEmailID').send_keys(users[i])
            self.driver.find_element_by_accessibility_id('textFieldPasswordID').send_keys('Test@123')
            login = self.driver.find_element_by_accessibility_id('buttonSignInID')
            #print 'Scenario.begin load_login'
            login.click()
            homepage = self.driver.find_element_by_accessibility_id('KOHLS_LOGO_ID')
            #print 'Scenario.end load_login'

            time.sleep(2)


                #try:
                #self.driver.find_element_by_accessibility_id('close modal').click()
                #except:
                #pass
                #time.sleep(2)
                #homepage = self.driver.find_element_by_accessibility_id('KOHLS_LOGO_ID')
            homepage.click()
            # potential kohls cash popup
            #try:
            #    self.driver.find_element_by_accessibility_id('KohlsCash Reminder Close').click()
            #except:
            #    pass
            # todo: temp fix for now. once we can split the bookends up some more, we can handle the
            # size selection screen via try/except (just don't include in bookend)

            self.driver.find_element_by_accessibility_id('textFieldSearchBarID').send_keys('shirts')
            pmp = self.driver.find_element_by_ios_predicate('type == "XCUIElementTypeButton" AND name == "Search"')
            time.sleep(5)
            print 'Scenario.begin load_products'
            pmp.click()
            shirts = self.driver.find_element_by_ios_predicate('type == "XCUIElementTypeStaticText" AND name == "lblProductNameID"')
            print 'Scenario.end load_products'
    
            time.sleep(2)
            print 'Scenario.begin load_product_details'
            shirts.click()
            #self.driver.find_element_by_name('imagePDPCarouselID')


            time.sleep(2)
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            self.driver.find_element_by_accessibility_id('buttonAddToCartID').click()
            try:
                self.driver.find_element_by_accessibility_id('2T').click()
            except:
                pass
            # assertion
            self.driver.find_element_by_accessibility_id('Item added to Cart')
            self.driver.find_element_by_accessibility_id('ShoppingBagButtonId').click()
            self.driver.find_element_by_ios_predicate('type == "XCUIElementTypeStaticText" AND value CONTAINS[c] "Remove"')
            print 'Scenario.end load_product_details'

            time.sleep(2)
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            checkout = self.driver.find_element_by_accessibility_id('Checkout')
            checkout.click()
            self.driver.find_element_by_accessibility_id('3. Review').click()
            time.sleep(2)
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            time.sleep(1)
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})

            
            order = self.driver.find_element_by_accessibility_id('Place Order')
            print 'Scenario.begin load_checkout'
            order.click()
            self.driver.find_element_by_accessibility_id('Done')
            print 'Scenario.end load_checkout'

            #Wallet add.. 
            
            self.driver.find_element_by_accessibility_id('Done').click()
            time.sleep(2)
            self.driver.find_element_by_accessibility_id('MENU_OPEN_ID').click()
            time.sleep(2)
            walet = self.driver.find_element_by_accessibility_id('buttonWalletHeader')
            print 'Scenario.begin load_wallet'
            walet.click()
            time.sleep(10)
            kohlscash = self.driver.find_element_by_ios_predicate('type == "XCUIElementTypeStaticText" AND name == "labelTextKohlsWalletCellID"')
            kohlscash.click()
            self.driver.execute_script('mobile: swipe', {'direction': 'up'})
            self.driver.find_element_by_accessibility_id('imageBarCodeID')
            
            print 'Scenario.end load_wallet'
            time.sleep(2)
        finally:
            time.sleep(10)
            self.driver.quit()
