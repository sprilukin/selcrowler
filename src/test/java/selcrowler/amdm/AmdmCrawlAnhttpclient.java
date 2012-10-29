package selcrowler.amdm;

import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.driver.web.AnhttpclientWebDriverService;
import selcrowler.runner.AsyncScriptRunnerService;
import selcrowler.runner.ThreadPoolScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

public class AmdmCrawlAnhttpclient extends AmdmCrawlBase {
    private static AsyncScriptRunnerService scriptRunnerService;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        AnhttpclientWebDriverService driver = new AnhttpclientWebDriverService();
        driver.setEncoding("windows-1251");

        scriptRunnerService = new AsyncScriptRunnerService();
        scriptRunnerService.setThreadsCount(5);
        scriptRunnerService.setWebDriverService(driver);
    }


    protected String getBasePath() {
        return BASE_PATH + "_anhttp";
    }

    @Test
    public void testAmDmCrawling() throws Exception {
        scriptRunnerService.run(getLetters, new BindingImpl());
        scriptRunnerService.join();
    }
}
