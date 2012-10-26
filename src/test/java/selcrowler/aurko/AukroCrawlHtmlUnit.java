package selcrowler.aurko;

import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.driver.service.DriverServiceService;
import selcrowler.driver.web.HtmlUnitWebDriverService;
import selcrowler.runner.SingleThreadScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

public class AukroCrawlHtmlUnit extends AukroCrawlBase {
    private static SingleThreadScriptRunnerService scriptRunnerService;
    private static DriverServiceService service;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        HtmlUnitWebDriverService driver = new HtmlUnitWebDriverService();
        driver.setJavaScriptEnabled(false);

        scriptRunnerService = new SingleThreadScriptRunnerService();
        scriptRunnerService.setWebDriverService(driver);
    }

    @Test
    public void testGoogleSearch() throws Exception {
        scriptRunnerService.run(getTitles, new BindingImpl());
    }
}
