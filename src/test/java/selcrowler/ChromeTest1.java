package selcrowler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import selcrowler.driver.service.ChromeDriverServiceService;
import selcrowler.driver.service.DriverServiceService;
import selcrowler.driver.web.ChromeWebDriverService;
import selcrowler.runner.ScriptRunner;
import selcrowler.runner.ScriptRunnerService;
import selcrowler.runner.ThreadPoolScriptRunnerService;
import selcrowler.runner.binding.Binding;
import selcrowler.runner.binding.BindingImpl;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class ChromeTest1 {
    private static ThreadPoolScriptRunnerService scriptRunnerService;
    private static DriverServiceService service;

    @BeforeClass
    public static void createAndStartService() throws Exception {
        service = new ChromeDriverServiceService("d:\\chromedriver.exe");

        ChromeWebDriverService driver = new ChromeWebDriverService();
        driver.setDriverServiceService(service);

        scriptRunnerService = new ThreadPoolScriptRunnerService();
        scriptRunnerService.setThreadsCount(4);
        scriptRunnerService.setWebDriverService(driver);
    }

    @AfterClass
    public static void createAndStopService() {
        service.stop();
    }

    @Test
    public void testGoogleSearch() throws Exception {

        final AtomicInteger count = new AtomicInteger(0);
        final int max = 8;

        ScriptRunner scriptRunner = new ScriptRunner() {
            @Override
            public void callback(WebDriver driver, Binding bindings) throws Exception {
                driver.get("http://www.google.com");

                WebElement searchBox = driver.findElement(By.name("q"));
                searchBox.sendKeys("webdriver");

                WebElement submit = driver.findElement(By.xpath(".//button"));
                submit.click();

                (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return d.getTitle().toLowerCase().startsWith("webdriver");
                    }
                });

                assertTrue(driver.getTitle().startsWith("webdriver"));

                System.out.println(Thread.currentThread().getName());

                if (count.addAndGet(1) <= max) {
                    ScriptRunnerService s = bindings.get(ScriptRunnerService.class);
                    s.run(this, bindings);
                    s.run(this, bindings);
                }
            }
        };

        scriptRunnerService.run(scriptRunner, new BindingImpl());
        scriptRunnerService.join();
    }
}
