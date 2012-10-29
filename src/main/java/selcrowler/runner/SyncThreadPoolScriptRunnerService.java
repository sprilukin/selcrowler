package selcrowler.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import selcrowler.runner.binding.Binding;

public class SyncThreadPoolScriptRunnerService extends ThreadPoolScriptRunnerService {
    private static final Log log = LogFactory.getLog(ThreadPoolScriptRunnerService.class);

    @Override
    protected void runInternal(final ScriptRunner scriptRunner, final Binding binding) {
        createExecutorService();

        final Object monitor = new Object();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    scriptRunner.callback(webDriverService.getWebDriver(), binding);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    binding.put(EXCEPTION_KEY, e);
                } finally {
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            }
        });

        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                /* ignore */
            }
        }
    }
}
