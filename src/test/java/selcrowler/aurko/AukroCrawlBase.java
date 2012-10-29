package selcrowler.aurko;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selcrowler.runner.ScriptRunner;
import selcrowler.runner.binding.Binding;

import java.util.List;

/**
 * Crawler for aukro.ua
 *
 */
public abstract class AukroCrawlBase {

    private static final Log log = LogFactory.getLog(AukroCrawlBase.class);

    public static final String BASE_URL = "http://aukro.ua";

    //http://aukro.ua/listing/user.php/run?category=111916&change_view=1&us_id=23084438&view=gal
    public static final String LIST_USER_PATH = "/listing/user.php/run";

    public static enum ListUserParameters {
        CATEGORY("category"),
        USERID("us_id"),
        VIEW("view");

        private String name;

        ListUserParameters(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class UrlParameter {
        private ListUserParameters param;
        private String value;

        public UrlParameter(ListUserParameters param, String value) {
            this.param = param;
            this.value = value;
        }

        public String toParameter() {
            return String.format("%s=%s", this.param.getName(), this.value);
        }
    }

    public static String toParamString(UrlParameter... params) {
        StringBuilder sb = new StringBuilder();
        for (UrlParameter param: params) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(param.toParameter());
        }

        return sb.toString();
    }

    public static String getUrl(UrlParameter... params) {
        return String.format("%s%s?%s", BASE_URL, LIST_USER_PATH, toParamString(params));
    }

    public ScriptRunner getTitles = new ScriptRunner() {
        @Override
        public void callback(WebDriver driver, Binding bindings) throws Exception {
            //http://aukro.ua/listing/user.php/run?category=111916&change_view=1&us_id=23084438&view=gal
            UrlParameter userId = new UrlParameter(ListUserParameters.USERID, "23084438");
            UrlParameter category = new UrlParameter(ListUserParameters.CATEGORY, "52311");
            UrlParameter view = new UrlParameter(ListUserParameters.VIEW, "gal");
            driver.get(getUrl(userId, category, view));

            List<WebElement> items = driver.findElements(By.cssSelector("div.itemListResult"));
            for (WebElement item: items) {
            //for (int i = 0; i <= 0; i++) {
                WebElement titleSpan = item.findElement(By.cssSelector("a.iTitle span"));
                WebElement imgUrl = item.findElement(By.cssSelector("a.iImg"));
                WebElement price = item.findElement(By.cssSelector("span.iPriceR"));
                log.debug(String.format("[%s], [%s], [%s]", titleSpan.getText(), imgUrl.getAttribute("data-img"), price.getText()));
            }
        }
    };
}
