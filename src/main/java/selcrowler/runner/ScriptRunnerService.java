package selcrowler.runner;

import selcrowler.runner.binding.Binding;

public interface ScriptRunnerService {
    public static final String NAME = "scriptRunnerService";

    public void run(ScriptRunner scriptRunner, Binding binding);
}
