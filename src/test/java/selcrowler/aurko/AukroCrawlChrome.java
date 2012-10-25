package selcrowler.aurko;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import selcrowler.amdm.AmdmCrawlBase;
import selcrowler.driver.service.ChromeDriverServiceService;
import selcrowler.driver.service.DriverServiceService;
import selcrowler.driver.web.ChromeWebDriverService;
import selcrowler.runner.SingleThreadScriptRunnerService;
import selcrowler.runner.ThreadPoolScriptRunnerService;
import selcrowler.runner.binding.BindingImpl;

import java.util.Arrays;

public class AukroCrawlChrome extends AukroCrawlBase {
    private static SingleThreadScriptRunnerService scriptRunnerService;
    private static DriverServiceService service;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        service = new ChromeDriverServiceService("d:\\chromedriver.exe");

        ChromeWebDriverService driver = new ChromeWebDriverService();
        driver.setDriverServiceService(service);
        driver.setCommandLineArguments(Arrays.asList("--disable-extensions", "--disable-images"));

        scriptRunnerService = new SingleThreadScriptRunnerService();
        scriptRunnerService.setWebDriverService(driver);
    }

    @AfterClass
    public static void createAndStopService() {
        service.stop();
    }

    @Test
    public void testGoogleSearch() throws Exception {
        scriptRunnerService.run(getTitles, new BindingImpl());
    }
}
