package selcrowler.runner;

import selcrowler.driver.web.WebDriverService;
import selcrowler.runner.binding.Binding;

public abstract class BaseScriptRunnerService implements ScriptRunnerService {

    protected WebDriverService webDriverService;

    public void setWebDriverService(WebDriverService webDriverService) {
        this.webDriverService = webDriverService;
    }

    protected void prepareBinding(Binding binding) {
        if (binding != null) {
            binding.put(ScriptRunnerService.NAME, this, ScriptRunnerService.class);
        }
    }

    @Override
    public void run(ScriptRunner scriptRunner, Binding binding) {
        prepareBinding(binding);
        runInternal(scriptRunner, binding);
    }

    protected abstract void runInternal(ScriptRunner scriptRunner, Binding binding);
}
