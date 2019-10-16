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
        //users[1] = "eliteuser6@loy.com";
        //users[2] = "eliteuser9@loy.com";
        //users[3] = "eliteuser25@loy.com";

        int userSelect = (int) Math.random()*3;

        Thread.sleep(10000);
        //Log.i("NimbleDroidV1", "Scenario.begin load_Homepage");
        showAcceptTerm();
        // onWebView().withElement(findElement(Locator.ID, "Yes2YouSignUp_id")).;
        Thread.sleep(10000);

        //Log.i("NimbleDroidV1", "Scenario.end load_Homepage");

        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_actionBar_list_icon")).click();
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/tv_sign_in")).click();
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_signIn_EmailEditText")).setText("eliteuser16@loy.com");
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_signIn_PasswordEditText")).setText("Test@123");
        UiObject login = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_signIn_SignInButton"));
        //Log.i("NimbleDroidV1", "Scenario.begin load_login");
        login.click();

        UiObject rodger = mDevice.findObject(new UiSelector().text("Wallet"));
        rodger.waitForExists(10000);





        //Code added for wallet
        Log.i("NimbleDroidV1", "Scenario.begin wallet");
        mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/user_account_wallet")).click();
        UiObject khs = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/value_to_set_kc"));
        khs.waitForExists(10000);
        Log.i("NimbleDroidV1", "Scenario.end wallet");
        Thread.sleep(5000);
        try {
            mDevice.findObject(new UiSelector().text("Cancel")).click();
        } catch(Exception e) {
            ;
        }

        Log.i("NimbleDroidV1", "Scenario.begin KohlsCash");
        khs.click();
        UiObject kohls = mDevice.findObject(new UiSelector().text("KOHL'S CASH"));
        kohls.waitForExists(10000);
        kohls.swipeUp(100);
        assert_element(mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE+":id/wallet_title")), 10000);
        Log.i("NimbleDroidV1", "Scenario.end KohlsCash");
    }
}

