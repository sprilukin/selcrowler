package selcrowler.driver.service;

import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;
import java.net.URL;

public abstract class BaseLocalDriverServiceService implements DriverServiceService {

    protected String pathToDriver;
    protected DriverService service;

    public BaseLocalDriverServiceService() {
    }

    public BaseLocalDriverServiceService(String pathToDriver) {
        this.pathToDriver = pathToDriver;
    }

    public void setPathToDriver(String pathToDriver) {
        this.pathToDriver = pathToDriver;
    }

    protected abstract void start() throws IOException;

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
