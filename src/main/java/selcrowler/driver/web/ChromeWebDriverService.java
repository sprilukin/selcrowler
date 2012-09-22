package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

public class ChromeWebDriverService extends RemoteWebDriverService {

    private List<String> commandLineArguments = new ArrayList<String>();

    public void setCommandLineArguments(List<String> commandLineArguments) {
        this.commandLineArguments = commandLineArguments;
    }

    @Override
    protected WebDriver createWebDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.switches", commandLineArguments);

        RemoteWebDriver wrappedWebDriver = new RemoteWebDriver(driverServiceService.getServiceUrl(), capabilities);
        return new WebDriverWrapper(wrappedWebDriver);
    }
}
