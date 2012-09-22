package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class ChromeWebDriverService extends ThreadLocalWebDriverService {

    @Override
    protected WebDriver createWebDriver(URL url) {
        RemoteWebDriver wrappedWebDriver = new RemoteWebDriver(url, DesiredCapabilities.chrome());
        return new WebDriverWrapper(wrappedWebDriver);
    }
}
