package selcrowler.driver.service;

import org.openqa.selenium.ie.InternetExplorerDriverService;

import java.io.File;
import java.io.IOException;

public class IEDriverServiceService extends BaseLocalDriverServiceService {

    public IEDriverServiceService() {
    }

    public IEDriverServiceService(String pathToDriver) {
        super(pathToDriver);
    }

    protected synchronized void start() throws IOException {
        if (service == null) {
            File file = new File(pathToDriver);
            service = new InternetExplorerDriverService.Builder()
                    .usingDriverExecutable(file)
                    .usingAnyFreePort()
                    .build();

            service.start();
        }
    }
}
