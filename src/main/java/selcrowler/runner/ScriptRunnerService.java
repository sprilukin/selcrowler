package selcrowler.runner;

import selcrowler.runner.binding.Binding;

public interface ScriptRunnerService {
    public static final String NAME = "scriptRunnerService";
    public static final String EXCEPTION_KEY = "exception";

    public void run(ScriptRunner scriptRunner, Binding binding);
}
