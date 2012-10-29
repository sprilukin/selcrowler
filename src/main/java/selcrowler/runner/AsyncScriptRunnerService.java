package selcrowler.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import selcrowler.runner.binding.Binding;

import java.util.concurrent.atomic.AtomicInteger;

public class AsyncScriptRunnerService extends ThreadPoolScriptRunnerService {
    private static final Log log = LogFactory.getLog(ThreadPoolScriptRunnerService.class);

    private final AtomicInteger tasksInQueue = new AtomicInteger(0);

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
                    binding.put(EXCEPTION_KEY, e);
                } finally {
                    tasksInQueue.decrementAndGet();

                    synchronized (tasksInQueue) {
                        tasksInQueue.notify();
                    }
                }
            }
        });
    }
}
