package selcrowler.driver.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.io.IOException;

public class FireFoxWebDriverService extends ThreadLocalWebDriverService {

    private static final Log log = LogFactory.getLog(FireFoxWebDriverService.class);

    @Override
    protected WebDriver createWebDriver() {
        try {
            File file = new File("d:\\webdriver.xpi");
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.addExtension(file);
            //firefoxProfile.setPreference("extensions.firebug.currentVersion", "1.8.1"); // Avoid startup screen

            WebDriver driver = new FirefoxDriver(firefoxProfile);
            return new WebDriverWrapper(driver);
        } catch (IOException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
