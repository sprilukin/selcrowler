package selcrowler.driver.service;

import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.File;
import java.io.IOException;

public class ChromeDriverServiceService extends BaseLocalDriverServiceService {

    public ChromeDriverServiceService() {
    }

    public ChromeDriverServiceService(String pathToDriver) {
        super(pathToDriver);
    }

    protected synchronized void start() throws IOException {
        if (service == null) {
            File file = new File(pathToDriver);
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(file)
                    .usingAnyFreePort()
                    .build();

            service.start();
        }
    }
}
