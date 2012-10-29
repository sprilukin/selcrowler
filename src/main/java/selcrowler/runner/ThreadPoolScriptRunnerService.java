package selcrowler.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import selcrowler.driver.web.WebDriverService;
import selcrowler.runner.binding.Binding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ThreadPoolScriptRunnerService extends BaseScriptRunnerService {

    private static final Log log = LogFactory.getLog(ThreadPoolScriptRunnerService.class);

    private int threadsCount = 4;
    protected ExecutorService executorService;

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    protected void createExecutorService() {
        if (executorService == null) {
            synchronized (this) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(threadsCount);
                }
            }
        }
    }
}
