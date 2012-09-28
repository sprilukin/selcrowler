package selcrowler.driver.anhttpclient;

import anhttpclient.WebBrowser;
import anhttpclient.WebResponse;
import anhttpclient.impl.DefaultWebBrowser;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.browserlaunchers.Proxies;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.openqa.selenium.remote.CapabilityType.SUPPORTS_FINDING_BY_CSS;

public class AnhttpclientDriver implements WebDriver, /*JavascriptExecutor,*/
        FindsById, FindsByLinkText, FindsByXPath, FindsByName, FindsByCssSelector,
        FindsByTagName, HasCapabilities/*, HasInputDevices*/ {

    public static final String USER_AGENT_HTTP_HEADER = "User-Agent";

    private String encoding = "UTF-8";
    private WebBrowser webBrowser;
    private ThreadLocal<WebResponse> webResponse = new ThreadLocal<WebResponse>();
    private ThreadLocal<Document> document = new ThreadLocal<Document>();

    public AnhttpclientDriver() {
        this((BrowserVersion)null);
    }

    public AnhttpclientDriver(BrowserVersion browserVersion) {
        webBrowser = new DefaultWebBrowser(true);

        if (browserVersion != null) {
            webBrowser.addHeader(USER_AGENT_HTTP_HEADER, browserVersion.getUserAgent());
        }
    }

    public AnhttpclientDriver(Capabilities capabilities) {
        this(determineBrowserVersion(capabilities));

        if (capabilities.getCapability(CapabilityType.PROXY) != null) {
            Proxy proxy = Proxies.extractProxy(capabilities);
            String fullProxy = proxy.getHttpProxy();
            if (fullProxy != null) {
                int index = fullProxy.indexOf(":");
                if (index != -1) {
                    String host = fullProxy.substring(0, index);
                    int port = Integer.parseInt(fullProxy.substring(index + 1));
                    setProxy(host, port);
                } else {
                    setProxy(fullProxy, 80);
                }
            }
        }
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    // Package visibility for testing
    static BrowserVersion determineBrowserVersion(Capabilities capabilities) {
        String browserName = null;
        String browserVersion = null;

        String rawVersion = capabilities.getVersion();
        String[] splitVersion = rawVersion == null ? new String[0] : rawVersion.split("-");
        if (splitVersion.length > 1) {
            browserVersion = splitVersion[1];
            browserName = splitVersion[0];
        } else {
            browserName = capabilities.getVersion();
            browserVersion = "";
        }

        // This is for backwards compatibility - in case there are users who are trying to
        // configure the HtmlUnitDriver by using the c'tor with capabilities.
        if (!"htmlunit".equals(capabilities.getBrowserName())) {
            browserName = capabilities.getBrowserName();
            browserVersion = capabilities.getVersion();
        }

        if ("firefox".equals(browserName)) {
            return BrowserVersion.FIREFOX_3_6;
        }

        if ("internet explorer".equals(browserName)) {
            // Try and convert the version
            try {
                int version = Integer.parseInt(browserVersion);
                switch (version) {
                    case 6:
                        return BrowserVersion.INTERNET_EXPLORER_6;
                    case 7:
                        return BrowserVersion.INTERNET_EXPLORER_7;
                    case 8:
                        return BrowserVersion.INTERNET_EXPLORER_8;
                    default:
                        return BrowserVersion.INTERNET_EXPLORER_8;
                }
            } catch (NumberFormatException e) {
                return BrowserVersion.INTERNET_EXPLORER_8;
            }
        }

        return BrowserVersion.FIREFOX_3_6;
    }

    public void setProxy(String host, int port) {
        webBrowser.setProxy(host, port);
    }

    public Capabilities getCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities("anhttpclient", "", Platform.ANY);

        capabilities.setPlatform(Platform.getCurrent());
        capabilities.setJavascriptEnabled(false);
        capabilities.setCapability(SUPPORTS_FINDING_BY_CSS, true);

        return capabilities;
    }

    public void get(String url) {
        try {
            webResponse.set(webBrowser.getResponse(completeUrl(url), encoding));
            document.set(Jsoup.parse(webResponse.get().getText()));
        } catch (IOException e) {
            throw new WebDriverException(e);
        }
    }

    private String completeUrl(String url) {
        if (url.startsWith("/")) {
            try {
                return "http://" + webResponse.get().getUrl().getHost() + url;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return url;
        }
    }

    public String getCurrentUrl() {
        try {
            return webResponse.get().getUrl().toString();
        } catch (MalformedURLException e) {
            throw new WebDriverException(e);
        }
    }

    public String getTitle() {
        return document.get().title();
    }

    private WebElement findElement(final By locator, final SearchContext context) {
        return locator.findElement(context);
    }

    protected List<WebElement> findElements(final By by, final SearchContext context) {
        return by.findElements(context);
    }

    public WebElement findElement(By by) {
        return findElement(by, this);
    }

    public List<WebElement> findElements(By by) {
        return findElements(by, this);
    }

    public String getPageSource() {
        try {
            return webResponse.get().getText();
        } catch (UnsupportedEncodingException e) {
            throw new WebDriverException(e);
        }
    }

    public void close() {
        //do nothing
        webResponse.set(null);
    }

    public void quit() {
        webBrowser = null;
    }

    public Set<String> getWindowHandles() {
        throw new IllegalArgumentException("Operation not supported");
    }

    public String getWindowHandle() {
        throw new IllegalArgumentException("Operation not supported");
    }

    public TargetLocator switchTo() {
        throw new IllegalArgumentException("Operation not supported");
    }

    public Navigation navigate() {
        throw new IllegalArgumentException("Operation not supported");
    }

    public WebElement findElementByLinkText(String selector) {
        //TODO
        throw new IllegalArgumentException("not implemented");
    }

    public List<WebElement> findElementsByLinkText(String selector) {
        //TODO
        throw new IllegalArgumentException("not implemented");
    }

    public WebElement findElementById(String id) {
        //TODO
        throw new IllegalArgumentException("not implemented");
    }

    public List<WebElement> findElementsById(String id) {
        return findElementsByXPath("//*[@id='" + id + "']");
    }

    public WebElement findElementByCssSelector(String using) {
        return new AnhttpclientWebElement(document.get().select(using).first(), this);
    }

    public List<WebElement> findElementsByCssSelector(String using) {
        return AnhttpclientWebElement.getWebElements(document.get().select(using), this);
    }

    public WebElement findElementByName(String name) {
        //TODO
        return null;
    }

    public List<WebElement> findElementsByName(String using) {
        //TODO
        return null;
    }

    public WebElement findElementByTagName(String name) {
        return new AnhttpclientWebElement(document.get().getElementsByTag(name).first(), this);
    }

    public List<WebElement> findElementsByTagName(String using) {
        return AnhttpclientWebElement.getWebElements(document.get().getElementsByTag(using), this);
    }

    public WebElement findElementByXPath(String selector) {
        //TODO
        return null;
    }

    public List<WebElement> findElementsByXPath(String selector) {
        //TODO
        return null;
    }

    public Options manage() {
        return new AnhttpclientOptions();
    }

    private class AnhttpclientOptions implements Options {

        public Logs logs() {
            throw new UnsupportedOperationException("Driver does not support this operation.");
        }

        public void addCookie(Cookie cookie) {
            BasicClientCookie clientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
            clientCookie.setPath(cookie.getPath());
            clientCookie.setExpiryDate(cookie.getExpiry());
            clientCookie.setSecure(cookie.isSecure());
            clientCookie.setDomain(getDomainForCookie());

            webBrowser.addCookie(clientCookie);
        }

        public Cookie getCookieNamed(String name) {
            Set<Cookie> allCookies = getCookies();
            for (Cookie cookie : allCookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }

            return null;
        }

        public void deleteCookieNamed(String name) {
            throw new IllegalArgumentException("Method not supported");
        }

        public void deleteCookie(Cookie cookie) {
            throw new IllegalArgumentException("Method not supported");
        }

        public void deleteAllCookies() {
            webBrowser.clearAllCookies();
        }

        public Set<Cookie> getCookies() {
            return new HashSet<Cookie>((Collection<Cookie>)CollectionUtils.collect(webBrowser.getCookies(), new Transformer() {
                @Override
                public Object transform(Object input) {
                    org.apache.http.cookie.Cookie aCookie = (org.apache.http.cookie.Cookie)input;
                    return new Cookie(
                            aCookie.getName(), aCookie.getValue(), aCookie.getDomain(),
                            aCookie.getPath(), aCookie.getExpiryDate(), aCookie.isSecure());
                }
            }));
        }


        private String getDomainForCookie() {
            try {
                return webResponse.get().getUrl().getHost();
            } catch (MalformedURLException e) {
                throw new WebDriverException(e);
            }
        }

        public Timeouts timeouts() {
            throw new UnsupportedOperationException("not supported.");
        }

        public ImeHandler ime() {
            throw new UnsupportedOperationException("Cannot input IME using HtmlUnit.");
        }

        public Window window() {
            throw new UnsupportedOperationException("Window handling not yet implemented in HtmlUnit");
        }

    }

    public WebElement findElementByPartialLinkText(String using) {
        //TODO
        return null;
    }

    public List<WebElement> findElementsByPartialLinkText(String using) {
        //TODO
        return null;
    }
}
