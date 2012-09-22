package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import selcrowler.driver.service.DriverServiceService;

import java.net.URL;

public abstract class ThreadLocalWebDriverService implements WebDriverService {

    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>() {
        @Override
        protected synchronized WebDriver initialValue() {
            return createWebDriver();
        }
    };

    protected abstract WebDriver createWebDriver();

    @Override
    public WebDriver getWebDriver() {
        return webDriver.get();
    }
}
