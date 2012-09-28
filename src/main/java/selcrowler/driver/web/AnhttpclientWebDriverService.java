package selcrowler.driver.web;

import org.openqa.selenium.WebDriver;
import selcrowler.driver.anhttpclient.AnhttpclientDriver;

public class AnhttpclientWebDriverService extends ThreadLocalWebDriverService {

    private String encoding = "UTF-8";

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected WebDriver createWebDriver() {
        AnhttpclientDriver driver = new AnhttpclientDriver();
        driver.setEncoding(encoding);

        return new WebDriverWrapper(driver);
    }
}
