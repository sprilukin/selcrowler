package selcrowler.driver.anhttpclient;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.ArrayList;
import java.util.List;

public class AnhttpclientWebElement implements WrapsDriver,
        FindsById, FindsByLinkText, FindsByXPath, FindsByTagName,
        FindsByCssSelector, Locatable, WebElement {

    private Element element;
    private WebDriver driver;

    public AnhttpclientWebElement(Element element, WebDriver driver) {
        this.element = element;
        this.driver = driver;
    }

    @Override
    public void click() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public void submit() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public void clear() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public String getTagName() {
        return element.tagName();
    }

    @Override
    public String getAttribute(String name) {
        return element.attr(name);
    }

    @Override
    public boolean isSelected() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public boolean isEnabled() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public String getText() {
        return element.text();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return by.findElements(this);
    }

    @Override
    public WebElement findElement(By by) {
        return by.findElement(this);
    }

    @Override
    public boolean isDisplayed() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public Point getLocation() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public Dimension getSize() {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public String getCssValue(String propertyName) {
        throw new IllegalArgumentException("not supported");
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        return new AnhttpclientWebElement(element.select(using).first(), driver);
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return getWebElements(element.select(using), driver);
    }

    public static List<WebElement> getWebElements(Elements select, WebDriver webDriver) {
        List<WebElement> elements = new ArrayList<WebElement>(select.size());
        for (Element element: select) {
            elements.add(new AnhttpclientWebElement(element, webDriver));
        }

        return elements;
    }

    @Override
    public WebElement findElementById(String using) {
        return new AnhttpclientWebElement(element.getElementById(using), driver);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return getWebElements(element.getElementsByAttribute("id"), driver);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public WebElement findElementByTagName(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public WebElement findElementByXPath(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public Point getLocationOnScreenOnceScrolledIntoView() {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public Coordinates getCoordinates() {
        //TODO: implement
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
