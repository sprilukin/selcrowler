package selcrowler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selcrowler.runner.ScriptRunner;
import selcrowler.runner.ScriptRunnerService;
import selcrowler.runner.binding.Binding;
import selcrowler.runner.binding.BindingImpl;

import java.util.List;

public class AmdmCrawlBase {

    private static final Log log = LogFactory.getLog(AmdmCrawlChrome.class);

    private static final String AMDM = "http://amdm.ru";
    private static final String SKIP_PAGINATION = "SKIP_PAGINATION";

    public static ScriptRunner getLetters = new ScriptRunner() {
        @Override
        public void callback(WebDriver driver, Binding bindings) throws Exception {
            driver.get(AMDM);

            ScriptRunnerService s = bindings.get(ScriptRunnerService.class);
            List<WebElement> letters = driver.findElements(By.cssSelector("table table td.chords a"));
            for (WebElement letter: letters) {
                String path = letter.getAttribute("href");
                s.run(getArtistsForLetter, new BindingImpl()
                        .add("path", path)
                        .add("letter", letter.getText())
                        .add(SKIP_PAGINATION, Boolean.FALSE));
                break;
            }
        }
    };

    public static ScriptRunner getArtistsForLetter = new ScriptRunner() {

        @Override
        public void callback(WebDriver driver, Binding bindings) throws Exception {
            String path = bindings.get(String.class, "path");
            log.debug("getArtistsForLetter: " + path);

            driver.get(path);

            ScriptRunnerService srs = bindings.get(ScriptRunnerService.class);
            if (!bindings.get(Boolean.class, SKIP_PAGINATION)) {
                List<WebElement> anchors = driver.findElements(By.cssSelector("table[width='790'] td[valign='top'] > a"));
                CollectionUtils.filter(anchors, new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        String href = ((WebElement) object).getAttribute("href");
                        return href.matches(".*/chords/[\\d]+/?page=[\\d]+") && !href.endsWith("/?page=0");
                    }
                });

                for (WebElement element: anchors) {
                    Binding binding = new BindingImpl()
                            .add("path", element.getAttribute("href"))
                            .add("letter", bindings.get("letter"))
                            .add(SKIP_PAGINATION, Boolean.TRUE);

                    srs.run(getArtistsForLetter, binding);
                    break;
                }
            }

            List<WebElement> artists = driver.findElements(By.cssSelector("table.border a"));
            for (WebElement element: artists) {
                Binding binding = new BindingImpl()
                        .add("path", element.getAttribute("href"))
                        .add("letter", bindings.get("letter"))
                        .add("artist", element.getText());

                srs.run(getSongsForArtist, binding);
                break;
            }
        }
    };

    public static ScriptRunner getSongsForArtist = new ScriptRunner() {

        @Override
        public void callback(WebDriver driver, Binding bindings) throws Exception {

            String path = bindings.get(String.class, "path");
            log.debug("getSongsForArtist: " + path);

            driver.get(path);

            WebElement table = driver.findElement(By.cssSelector("table[width='600']"));
            List<WebElement> songs = table.findElements(By.cssSelector("a[target='_blank']"));
            ScriptRunnerService srs = bindings.get(ScriptRunnerService.class);

            for (WebElement webElement: songs) {
                Binding binding = new BindingImpl()
                        .add("path", webElement.getAttribute("href"))
                        .add("letter", bindings.get("letter"))
                        .add("artist", bindings.get("artist"))
                        .add("song", webElement.getText());
                srs.run(getSong, binding);
                break;
            }
        }
    };

    public static ScriptRunner getSong = new ScriptRunner() {

        @Override
        public void callback(WebDriver driver, Binding bindings) throws Exception {
            String path = bindings.get(String.class, "path");
            log.debug("getSong: " + path);

            driver.get(path);

            String contents = driver.findElement(By.tagName("pre")).getText();
            log.debug(contents);
            log.debug(String.format("/%s/%s/%s", bindings.get("letter"), bindings.get("artist"), bindings.get("song")));
        }
    };
}
