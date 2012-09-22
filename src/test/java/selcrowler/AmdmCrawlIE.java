package selcrowler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.driver.service.DriverServiceService;
import selcrowler.driver.service.IEDriverServiceService;
import selcrowler.driver.web.ChromeWebDriverService;
import selcrowler.runner.ThreadPoolScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

public class AmdmCrawlIE extends AmdmCrawlBase {
    private static ThreadPoolScriptRunnerService scriptRunnerService;
    private static DriverServiceService service;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        service = new IEDriverServiceService("d:\\IEDriverServer.exe");

        ChromeWebDriverService driver = new ChromeWebDriverService();
        driver.setDriverServiceService(service);

        scriptRunnerService = new ThreadPoolScriptRunnerService();
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
