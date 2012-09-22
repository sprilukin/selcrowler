package selcrowler.driver.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

public class WebDriverWrapper implements WebDriver {
    private WebDriver wrappedWebDriver;

    public WebDriverWrapper(WebDriver wrappedWebDriver) {
        this.wrappedWebDriver = wrappedWebDriver;
    }

    @Override
    public void get(String url) {
        wrappedWebDriver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return wrappedWebDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return wrappedWebDriver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return wrappedWebDriver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return wrappedWebDriver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return wrappedWebDriver.getPageSource();
    }

    @Override
    public void close() {
        throw new RuntimeException("Method not allowed");
    }

    @Override
    public void quit() {
        throw new RuntimeException("Method not allowed");
    }

    @Override
    public Set<String> getWindowHandles() {
        return wrappedWebDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return wrappedWebDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return wrappedWebDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return wrappedWebDriver.navigate();
    }

    @Override
    public Options manage() {
        return wrappedWebDriver.manage();
    }
}
