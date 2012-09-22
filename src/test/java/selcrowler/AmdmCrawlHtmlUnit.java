package selcrowler;

import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.driver.web.HtmlUnitWebDriverService;
import selcrowler.driver.web.WebDriverService;
import selcrowler.runner.ThreadPoolScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

public class AmdmCrawlHtmlUnit extends AmdmCrawlBase {
    private static ThreadPoolScriptRunnerService scriptRunnerService;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        WebDriverService driver = new HtmlUnitWebDriverService();

        scriptRunnerService = new ThreadPoolScriptRunnerService();
        scriptRunnerService.setThreadsCount(1);
        scriptRunnerService.setWebDriverService(driver);
    }


    @Test
    public void testAmDmCrawling() throws Exception {
        scriptRunnerService.run(getLetters, new BindingImpl());
        scriptRunnerService.join();
    }
}
