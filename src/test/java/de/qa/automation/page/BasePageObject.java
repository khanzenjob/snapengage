package de.qa.automation.page;

import de.qa.automation.model.BrowserFactory;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePageObject {

     public WebDriverWait wait; //changed to public for access to SignInTestCase

    protected BrowserFactory browser;

    public BasePageObject(){

    }

    public BasePageObject(BrowserFactory browser) {
        this.browser = browser;
        PageFactory.initElements(this.browser.getWebDriver(), this);
        wait = browser.getWebDriverWait();
    }

}
