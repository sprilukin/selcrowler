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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AmdmCrawlBase {

    private static final Log log = LogFactory.getLog(AmdmCrawlChrome.class);

    private static final String BASE_PATH = "d:\\music\\text\\z_selenium\\amdm";
    private static final String AMDM = "http://amdm.ru";
    private static final String SKIP_PAGINATION = "SKIP_PAGINATION";

    private static final String FILE_NAME_FORBIDDEN_SYMBOLS = "[\\?]+";
    private static final String FILE_NAME_FORBIDDEN_SYMBOLS_REPLACEMENT = "_";

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

            Object letter = bindings.get("letter");
            Object artist = bindings.get("artist");
            Object song = bindings.get("song");
            log.debug(String.format("/%s/%s/%s", letter, artist, song));

            createDirIfNotExist(String.format("%s\\%s\\%s", BASE_PATH, letter, artist));
            saveStringToFile(contents, String.format("%s\\%s\\%s\\%s.txt", BASE_PATH, letter, artist, song));
        }
    };

    public static void saveStringToFile(String string, String path) throws IOException {
        FileWriter fw = new FileWriter(removeIllegalCharacters(path));
        fw.write(string);
        fw.flush();
        fw.close();
    }

    public static String removeIllegalCharacters(String fileName) {
        return fileName != null
                ? fileName.replaceAll(
                FILE_NAME_FORBIDDEN_SYMBOLS,
                FILE_NAME_FORBIDDEN_SYMBOLS_REPLACEMENT
        )
                : null;
    }

    public static void createDirIfNotExist(String dir) throws IOException {
        File directory = new File(dir);

        if (!directory.exists()) {
            Boolean result = directory.mkdirs();
            if (!result) {
                throw new IOException("Could not create directory: " + dir);
            }
        }
    }
}
