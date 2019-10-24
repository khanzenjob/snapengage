package de.qa.automation.page.preLogin;

import de.qa.automation.model.BrowserFactory;
import de.qa.automation.page.BasePageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LandingPage extends BasePageObject {

    @FindBy(how = How.ID, using = "email")
    private WebElement emailInputBox;

    @FindBy(how = How.ID, using = "password")
    private WebElement passwordInputBox;

    @FindBy(how = How.CSS, using = "input[type='submit']")
    private WebElement loginSubmit;

    @FindBy(how = How.CLASS_NAME, using = "avatar")
    private WebElement profile;

    @FindBy(how = How.CSS, using = "[class*=\"menu Dropdowncss\"] :nth-child(3)")
    private WebElement logoutMenuButton;

    @FindBy(how = How.CSS, using = "[class*=\"modal-footer\"] button[name=\"ok\"]")
    private WebElement logoutConfirmButton;

    public LandingPage(BrowserFactory browser) {
        super(browser);
    }

    public void login(String username, String password) {
        emailInputBox.sendKeys(username);
        passwordInputBox.sendKeys(password);
        loginSubmit.click();
    }

    public void logout(){
        wait.until(ExpectedConditions.visibilityOf(profile)).click();
        logoutMenuButton.click();
        logoutConfirmButton.click();
    }


}
