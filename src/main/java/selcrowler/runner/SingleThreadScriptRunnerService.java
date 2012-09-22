package selcrowler.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import selcrowler.runner.binding.Binding;

public class SingleThreadScriptRunnerService extends BaseScriptRunnerService {

    private static final Log log = LogFactory.getLog(SingleThreadScriptRunnerService.class);

    @Override
    protected void runInternal(final ScriptRunner scriptRunner, final Binding binding) {
        try {
            scriptRunner.callback(webDriverService.getWebDriver(), binding);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
