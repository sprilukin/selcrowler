package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitWebDriverService extends ThreadLocalWebDriverService {

    private boolean javaScriptEnabled = true;

    public void setJavaScriptEnabled(boolean javaScriptEnabled) {
        this.javaScriptEnabled = javaScriptEnabled;
    }

    @Override
    protected WebDriver createWebDriver() {
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(javaScriptEnabled);

        return new WebDriverWrapper(driver);
    }
}
