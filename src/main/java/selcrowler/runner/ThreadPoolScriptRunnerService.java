package selcrowler.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import selcrowler.driver.web.WebDriverService;
import selcrowler.runner.binding.Binding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolScriptRunnerService implements ScriptRunnerService {

    private static final Log log = LogFactory.getLog(ThreadPoolScriptRunnerService.class);

    private int threadsCount = 4;
    private ExecutorService executorService;
    private WebDriverService webDriverService;
    private final AtomicInteger tasksInQueue = new AtomicInteger(0);

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    public void setWebDriverService(WebDriverService webDriverService) {
        this.webDriverService = webDriverService;
    }

    public void join() throws InterruptedException {
        synchronized (tasksInQueue) {
            while (tasksInQueue.get() > 0) {
                tasksInQueue.wait();
            }
        }
    }

    @Override
    public void run(final ScriptRunner scriptRunner, final Binding binding) {
        if (threadsCount == 0) {
            runInSameThread(scriptRunner, binding);
        }


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

    private void runInSameThread(final ScriptRunner scriptRunner, final Binding binding) {
        try {
            scriptRunner.callback(webDriverService.getWebDriver(), binding);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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
