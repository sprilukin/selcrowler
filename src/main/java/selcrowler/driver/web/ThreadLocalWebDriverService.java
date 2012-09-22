package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import selcrowler.driver.service.DriverServiceService;

import java.net.URL;

public abstract class ThreadLocalWebDriverService implements WebDriverService {

    private DriverServiceService driverServiceService;

    private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>() {
        @Override
        protected synchronized WebDriver initialValue() {
            return createWebDriver(driverServiceService.getServiceUrl());
        }
    };

    public void setDriverServiceService(DriverServiceService driverServiceService) {
        this.driverServiceService = driverServiceService;
    }

    protected abstract WebDriver createWebDriver(URL url);

    @Override
    public WebDriver getWebDriver() {
        return webDriver.get();
    }
}
