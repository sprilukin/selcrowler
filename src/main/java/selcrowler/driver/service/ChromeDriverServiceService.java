package selcrowler.driver.service;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ChromeDriverServiceService implements DriverServiceService {

    private String pathToDriver;
    private DriverService service;

    public ChromeDriverServiceService() {
    }

    public ChromeDriverServiceService(String pathToDriver) {
        this.pathToDriver = pathToDriver;
    }

    public void setPathToDriver(String pathToDriver) {
        this.pathToDriver = pathToDriver;
    }

    public synchronized void start() throws IOException {
        if (service == null) {
            File file = new File(pathToDriver);
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(file)
                    .usingAnyFreePort()
                    .build();

            service.start();
        }
    }

    public synchronized void stop() {
        service.stop();
        service = null;
    }

    @Override
    public URL getServiceUrl() {
        if (service == null) {
            try {
                start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return service.getUrl();
    }
}
