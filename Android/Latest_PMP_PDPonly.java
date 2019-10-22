package com.nimbledroid.blackboxtest;
import android.os.Environment;
import android.support.test.espresso.Espresso;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;
import android.util.Log;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import android.view.View;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.sugar.Web.WebInteraction;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import android.support.test.espresso.web.webdriver.DriverAtoms;
import android.support.test.espresso.web.model.Atom;
import android.support.test.espresso.web.webdriver.Locator;

@RunWith(AndroidJUnit4.class)

public class EspressoTests {
    private static final String APP_PACKAGE = "com.kohls.mcommerce.opal";
    private static final String ACTIVITY_CLASS = "com.kohls.mcommerce.opal.framework.view.activity.KohlsSplashScreen";
    private static final String ELEMENT_NOT_FOUND = "Element not found - ";
    private static Class<? extends Activity> activityClass, activityClass2;
    static {
        try {
            activityClass = (Class<? extends Activity>) Class.forName(ACTIVITY_CLASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Rule
    public ActivityTestRule<?> activityTestRule = new ActivityTestRule<>(activityClass);

    // Grant read/write permission for android 6+ (API 23+)
    @Rule
    public GrantPermissionRule permissionRuleWrite = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule permissionRuleRead = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        /**
         * This method will take a screenshot upon failure, and save it into the
         * <SD card path>/failurescreenshots/ directory on the device
         *
         * @param e
         * @param description
         */
        @Override
        protected void failed(Throwable e, org.junit.runner.Description description) {
            // Create a directory to store screenshots if it doesn't already exist
            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/nimble/instrumentation/screenshot/");
            if (!path.exists()) {
                path.mkdirs();
            }

            // Take advantage of UiAutomator screenshot method
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String filename = "failure-" + APP_PACKAGE + "-" + description.getMethodName() + "-" + sdf.format(timestamp) + ".png";
            device.takeScreenshot(new File(path, filename));
        }
    };

    UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());;

    private UiObject assert_element(UiObject element, long timeout) throws Exception {
        if(!element.waitForExists(timeout)) {
            throw new UiObjectNotFoundException(ELEMENT_NOT_FOUND + element.getSelector().toString());
        }
        return element;
    }

    private WebInteraction assert_web_element(Atom element_selector, long timeout) throws Exception {
        long start_time = new Date().getTime();
        long end_time = start_time;

        while(end_time - start_time < timeout) {
            try {
                WebInteraction element = onWebView().withElement(element_selector);
                if(element != null) {
                    return element;
                }
            } catch(Exception e) {
                end_time = new Date().getTime();
            }
        }
        throw new RuntimeException(ELEMENT_NOT_FOUND + element_selector.toString());
    }

    private void showAcceptTerm() {
        try {
            Espresso.closeSoftKeyboard();
            mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_accept")).click();
            //mDevice.findObject(new UiSelector().resourceId("android:id/button2")).click();
            //Thread.sleep(2000);
            Log.i("NimbleDroidV1", "Scenario.begin load_Homepage");

            mDevice.findObject(new UiSelector().text("Cancel")).click();

        } catch (Exception e) {
            ;
        }
    }

    @Test
    public void test_shop() throws Exception {
        Log.i("NimbleDroidV1", "Scenario.profile");

        String[] users = new String[100];
        users[0] = "eliteuser16@loy.com";
        users[1] = "eliteuser6@loy.com";
        users[2] = "eliteuser9@loy.com";
        users[3] = "eliteuser25@loy.com";

        int userSelect = (int) Math.random()*3;

        Thread.sleep(10000);
        //Log.i("NimbleDroidV1", "Scenario.begin load_Homepage");
        showAcceptTerm();
        // onWebView().withElement(findElement(Locator.ID, "Yes2YouSignUp_id")).;
        Thread.sleep(5000);

        Log.i("NimbleDroidV1", "Scenario.end load_Homepage");

        /*mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_actionBar_list_icon")).click();
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/tv_sign_in")).click();
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_signIn_EmailEditText")).setText(users[userSelect]);
        Thread.sleep(5000);
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_signIn_PasswordEditText")).setText("Test@123");
        UiObject login = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_signIn_SignInButton"));
        Thread.sleep(10000);
        Log.i("NimbleDroidV1", "Scenario.begin load_login");
        login.click();*/

        //new version of app doesn't have the web pop up
        //mDevice.findObject(new UiSelector().className(android.webkit.WebView.class.getName())).waitForExists(40000);
        /*UiObject rodger = mDevice.findObject(new UiSelector().text("My Info"));
        rodger.waitForExists(10000);
        Log.i("NimbleDroidV1", "Scenario.end load_login");*/
        //Thread.sleep(6000);
        //mDevice.pressBack();

        // try to close the web view popup

        /*try {
            mDevice.findObject(new UiSelector().text("close modal")).click();
        } catch(Exception e) {
            ;
        }*/

        //mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_actionBar_app_image")).click();
        // try to close kohls cash popup
        /*try {
            mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/cross_img")).click();
        } catch(Exception e) {
            ;
        }*/
        // try to close system location permission popup
        try {
            mDevice.findObject(new UiSelector().text("Cancel")).click();
        } catch(Exception e) {
            ;
        }



        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_base_searchViewIcon")).click();
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_base_searchView")).setText("girls 7 16");
        Thread.sleep(5000);
        UiObject pmppage = mDevice.findObject(new UiSelector().text("girls 7 16 in Tops & Tees"));
        pmppage.waitForExists(10000);
        Log.i("NimbleDroidV1", "Scenario.begin load_products");

        pmppage.click();
        UiObject cp = mDevice.findObject(new UiSelector().text("Converse Hi-low Raglan Top - Girls 7-16"));
        cp.waitForExists(10000);
        Log.i("NimbleDroidV1", "Scenario.end load_products");


        UiObject product = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_product_itemImg"));
        /*Log.i("NimbleDroidV1", "Scenario.begin load_product_details");
        product.click();
        UiObject header_view = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/productDetails_headerView"));
        header_view.waitForExists(10000);
        header_view.swipeUp(10);
        if(header_view.waitForExists(1000)) {
            header_view.swipeUp(10);
        }
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_productDetails_addToBagStickyBtn")).click();
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_actionBar_item_image")).click();
        assert_element(mDevice.findObject(new UiSelector().description("Shopping bag Heading")), 7500);
        Log.i("NimbleDroidV1", "Scenario.end load_product_details");*/
        Log.i("NimbleDroidV1", "Scenario.begin load_product_details");
        product.click();
        //UiObject header_view = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/productDetails_titleTxt"));
        UiObject header_view = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/productDetails_headerView"));
        header_view.waitForExists(10000);
        Log.i("NimbleDroidV1", "Scenario.end load_product_details");

    }
}


