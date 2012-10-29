package selcrowler.amdm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.driver.service.DriverServiceService;
import selcrowler.driver.service.IEDriverServiceService;
import selcrowler.driver.web.IEWebDriverService;
import selcrowler.runner.AsyncScriptRunnerService;
import selcrowler.runner.ThreadPoolScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

public class AmdmCrawlIE extends AmdmCrawlBase {
    private static AsyncScriptRunnerService scriptRunnerService;
    private static DriverServiceService service;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        service = new IEDriverServiceService("d:\\IEDriverServer.exe");

        IEWebDriverService driver = new IEWebDriverService();
        driver.setDriverServiceService(service);

        scriptRunnerService = new AsyncScriptRunnerService();
        scriptRunnerService.setThreadsCount(2);
        scriptRunnerService.setWebDriverService(driver);
    }

    @AfterClass
    public static void createAndStopService() {
        service.stop();
    }

    @Test
    public void testGoogleSearch() throws Exception {
        scriptRunnerService.run(getLetters, new BindingImpl());
        scriptRunnerService.join();
    }
}
