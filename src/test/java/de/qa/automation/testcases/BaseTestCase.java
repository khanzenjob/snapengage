package de.qa.automation.testcases;

import de.qa.automation.helper.ConfigurationHelper;
import de.qa.automation.model.BrowserFactory;
import de.qa.automation.page.preLogin.LandingPage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class BaseTestCase {
    protected String environment = ConfigurationHelper.getInstance().getEnviroment();
    protected LandingPage landingPage;
    protected BrowserFactory browser;
    private int retries = ConfigurationHelper.getInstance().getPropertyInteger("retries", 3);
    private final static Logger log = Logger.getLogger(BaseTestCase.class);

    @Before
    public void setUp() {
        PropertyConfigurator.configure("./log4j.properties");//

        browser = new BrowserFactory();
        browser.startSnapEngage();
        landingPage = new LandingPage(browser);
    }

    @Rule
    public TestRule testRule = new TestRule() {

        public Statement apply(Statement base, Description description) {
            return statement(base, description);
        }

        private Statement statement(final Statement base, final Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    Throwable caughtThrowable = null;
                    for (int i = 0; i < retries; i++) {
                        try {
                            base.evaluate();
                            teardown();
                            return;
                        } catch (Throwable t) {
                            caughtThrowable = t;
                            log.error(caughtThrowable);
                            log.warn(description.getDisplayName() + ": run " + (i + 1) + " failed.");

                            if (ConfigurationHelper.getInstance().getPropertyBoolean("takeScreenshot")) {
                                String fileName = description.getMethodName() + "_try" + (i + 1);
                                String className = description.getClassName().split("de.qa.automation.testcases.")[1];
                                browser.takeScreenshot(className, fileName);
                            }
                            teardown();
                        }
                    }
                    System.err.println(description.getDisplayName() + ": giving up after " + retries + " failures.");
                    assert caughtThrowable != null;
                    throw caughtThrowable;
                }
            };
        }

        private void teardown() {
            browser.quitWebDriver();
        }


    };

}
