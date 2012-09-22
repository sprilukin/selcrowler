package selcrowler.driver.web;

import selcrowler.driver.service.DriverServiceService;

public abstract class RemoteWebDriverService extends ThreadLocalWebDriverService {

    protected DriverServiceService driverServiceService;

    public void setDriverServiceService(DriverServiceService driverServiceService) {
        this.driverServiceService = driverServiceService;
    }
}
