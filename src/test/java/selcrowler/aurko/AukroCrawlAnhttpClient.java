package selcrowler.aurko;

import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.driver.service.DriverServiceService;
import selcrowler.driver.web.AnhttpclientWebDriverService;
import selcrowler.driver.web.HtmlUnitWebDriverService;
import selcrowler.driver.web.WebDriverService;
import selcrowler.runner.SingleThreadScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

public class AukroCrawlAnhttpClient extends AukroCrawlBase {
    private static SingleThreadScriptRunnerService scriptRunnerService;
    private static DriverServiceService service;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        AnhttpclientWebDriverService driver = new AnhttpclientWebDriverService();
        driver.setEncoding("UTF-8");

        scriptRunnerService = new SingleThreadScriptRunnerService();
        scriptRunnerService.setWebDriverService(driver);
    }

    @Test
    public void testGoogleSearch() throws Exception {
        long startTime = System.currentTimeMillis();
        scriptRunnerService.run(getTitles, new BindingImpl());
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }
}
