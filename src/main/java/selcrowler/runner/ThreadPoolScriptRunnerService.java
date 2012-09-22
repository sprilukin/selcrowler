package selcrowler.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import selcrowler.driver.web.WebDriverService;
import selcrowler.runner.binding.Binding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolScriptRunnerService extends BaseScriptRunnerService {

    private static final Log log = LogFactory.getLog(ThreadPoolScriptRunnerService.class);

    private int threadsCount = 4;
    private ExecutorService executorService;
    private final AtomicInteger tasksInQueue = new AtomicInteger(0);

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    public void join() throws InterruptedException {
        synchronized (tasksInQueue) {
            while (tasksInQueue.get() > 0) {
                tasksInQueue.wait();
            }
        }
    }

    @Override
    protected void runInternal(final ScriptRunner scriptRunner, final Binding binding) {
        createExecutorService();

        tasksInQueue.incrementAndGet();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    scriptRunner.callback(webDriverService.getWebDriver(), binding);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    tasksInQueue.decrementAndGet();

                    synchronized (tasksInQueue) {
                        tasksInQueue.notify();
                    }
                }
            }
        });
    }

    private void createExecutorService() {
        if (executorService == null) {
            synchronized (this) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(threadsCount);
                }
            }
        }
    }
}
