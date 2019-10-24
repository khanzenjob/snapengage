package de.qa.automation.testcases;

import de.qa.automation.helper.ConfigurationHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SignInTestCase extends BaseTestCase {

    String username;
    String password;

    @Before
    public void init() {
            username = ConfigurationHelper.getInstance().getProperty("selenium.user");
            password = ConfigurationHelper.getInstance().getProperty("selenium.password");
    }

    @Test
    public void signInWithValidUser() {
        String expectedUrl = "https://snapengage-qa.appspot.com/hub";
        landingPage.login(username, password);
        browser.takeScreenshot("SignInTestCase","SignInWithValidUser");
        Assert.assertEquals(expectedUrl, browser.getCurrentUrl());
    }
}
