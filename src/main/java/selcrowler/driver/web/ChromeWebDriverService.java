package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChromeWebDriverService extends ThreadLocalWebDriverService {

    private List<String> commandLineArguments = new ArrayList<String>();

    public void setCommandLineArguments(List<String> commandLineArguments) {
        this.commandLineArguments = commandLineArguments;
    }

    @Override
    protected WebDriver createWebDriver(URL url) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.switches", commandLineArguments);

        RemoteWebDriver wrappedWebDriver = new RemoteWebDriver(url, capabilities);
        return new WebDriverWrapper(wrappedWebDriver);
    }
}
