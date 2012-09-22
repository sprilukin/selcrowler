package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class IEWebDriverService extends RemoteWebDriverService {

    @Override
    protected WebDriver createWebDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

        RemoteWebDriver wrappedWebDriver = new RemoteWebDriver(driverServiceService.getServiceUrl(), capabilities);
        return new WebDriverWrapper(wrappedWebDriver);
    }
}
