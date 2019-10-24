package de.qa.automation.model;

import de.qa.automation.helper.ConfigurationHelper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class provides the Webdriver for the given Browser
 */
public class BrowserFactory {
    private final String osArchitecture = System.getProperty("os.name").toLowerCase();
    private WebDriver driver;
    private WebDriverWait wait;


    public BrowserFactory() {
        loadWebDriver();
        setWebDriverWait();
    }

    /**
     * start the default landingpage
     */
    public void startSnapEngage() {
        driver.manage().deleteAllCookies();
        driver.get("https://snapengage-qa.appspot.com/signin?to=hub");
    }

    public void openLink(String URL) {
        driver.get(URL);
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public void quitWebDriver() {
        driver.quit();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * wait and scroll to element
     *
     * @param webElement - element to be scrolled
     */
    public void scrollToWithWait(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                wait.until(ExpectedConditions.elementToBeClickable(webElement)));
    }

    public void navigateBack() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.history.go(-1)");
    }

    public void scrollToClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
    }

    /**
     * scroll to element
     *
     * @param webElement - element to be scrolled
     */
    public void scrollTo(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

    /**
     * click ob element
     *
     * @param webElement - element to be clicked
     */
    public void clickOn(WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
    }

    /**
     * Loads the webdriver depending of the properties
     */
    private void loadWebDriver() {

        String browser = ConfigurationHelper.getInstance().getProperty("browser");
        String testenviroment = ConfigurationHelper.getInstance().getProperty("testenviroment");
        boolean headless = ConfigurationHelper.getInstance().getPropertyBoolean("headless");
        switch (testenviroment) {
            case "local":
                switch (browser) {
                    case "chrome":
                        driver = getChromeDriver(headless);
                        break;
                    case "firefox":
                        driver = getFirefoxDriver(headless);
                        driver.manage().window().maximize();
                        break;
                    case "edge":
                        driver = getEdgeDriver();
                        break;
                    case "opera":
                        driver = getOperaDriver();
                        break;
                    case "phantomjs":
                        driver = getPhantomJsDriver();
                        driver.manage().window().maximize();
                        break;
                }
                break;
            case "grid":
                driver = getSeleniumGridRemoteDriver();
                break;
            case "docker":
                driver = getDockerRemoteDriver(browser);
                //remoteDrive.setFileDetector(new LocalFileDetector());
                driver.manage().window().maximize();
                break;
        }
        driver.manage().deleteAllCookies();
    }

    /**
     * Retruns webdriver for Chrome
     *
     * @return - ChromeDriver
     */
    private WebDriver getChromeDriver(boolean isHeadless) {
        String path = "";
        if (osArchitecture.contains("windows")) {
            path = "driver/chromedriver/chromedriver_win32/chromedriver.exe";
        } else if (osArchitecture.contains("mac")) {
            path = "driver/chromedriver/chromedriver_mac64/chromedriver";
        } else if (osArchitecture.contains("linux")) {
            path = "driver/chromedriver/chromedriver_linux64/chromedriver";
        }

        System.setProperty("webdriver.chrome.driver", path);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--always-authorize-plugins");
        if (isHeadless) {
            chromeOptions.addArguments("--headless");
            //chromeOptions.addArguments("window-size=1920,1080");
        }
        chromeOptions.addArguments("--start-maximized");
        return new ChromeDriver(chromeOptions);
    }

    /**
     * Returns webdriver for Firefox
     *
     * @return - FirefoxDriver
     */
    private WebDriver getFirefoxDriver(boolean isHeadless) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        if (isHeadless) {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            firefoxOptions.setBinary(firefoxBinary);
        }

        String path = "driver/firefoxdriver/geckodriver";
        path = osArchitecture.contains("windows") ? path + ".exe" : path;

        System.setProperty("webdriver.gecko.driver", path);
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "logs/firefox_logs.txt");
        return new FirefoxDriver(firefoxOptions);
    }

    /**
     * Returns webdriver for Opera
     *
     * @return - OperaDriver
     */
    private WebDriver getOperaDriver() {
        String path = "driver/operadriver/operadriver_win64/operadriver.exe";
        System.setProperty("webdriver.opera.driver", path);
        DesiredCapabilities capabilities = DesiredCapabilities.opera();
        capabilities.setCapability("opera.binary", path);

        return new OperaDriver(capabilities);
    }

    //phantonjs wird nicht mehr supported
    private WebDriver getPhantomJsDriver() {
        String path = "driver/phantomjsdriver/phantomjs-2.1.1-windows/bin/phantomjs.exe";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path);

        return new PhantomJSDriver(caps);
    }

    /**
     * Returns webdriver for Edge Browser
     *
     * @return - EdgeDriver
     */
    private WebDriver getEdgeDriver() {
        System.setProperty("webdriver.edge.driver", "driver/microsoftedgedriver/MicrosoftWebDriver.exe");
        return new EdgeDriver();
    }

    /**
     * Returns remote webdriver for chrome
     *
     * @return - RemoteWebDriver
     */
    private WebDriver getSeleniumGridRemoteDriver() {
        URL url = null;
        try {
            url = new URL("http://localhost:4444/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setBrowserName("chrome");
        cap.setPlatform(Platform.WIN10);
        return new RemoteWebDriver(url, cap);
    }

    private WebDriver getDockerRemoteDriver(String browser) {
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4444/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DesiredCapabilities cap = null;
        if (browser.equals("chrome")) {
            cap = DesiredCapabilities.chrome();
            cap.setBrowserName("chrome");
        } else if (browser.equals("firefox")) {
            cap = DesiredCapabilities.firefox();
            cap.setBrowserName("firefox");
        } else if (browser.equals("phantomjs")) {
            //phantonjs wird nicht mehr supported
            cap = DesiredCapabilities.phantomjs();
            cap.setBrowserName("phantomjs");
        }
        if (cap != null) {
            cap.setPlatform(Platform.LINUX);
        }
        return new RemoteWebDriver(url, cap);
    }

    /**
     * Sets the WebDriverWait to 10 seconds
     */
    private void setWebDriverWait() {
        wait = new WebDriverWait(driver, 10);
    }

    /**
     * Freezes the webdriver
     *
     * @param millis - milliseconds of freezing
     */
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the WebDriverWait Instance
     *
     * @return - WebDriverWait
     */
    public WebDriverWait getWebDriverWait() {
        return wait;
    }


    public void takeScreenshot(String className, String methodName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename = className + "/" +  "_" + methodName + ".png";
        try {
            FileUtils.copyFile(scrFile, new File(
                    "screenshots/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
