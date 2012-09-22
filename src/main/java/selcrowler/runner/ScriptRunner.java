package selcrowler.runner;

import org.openqa.selenium.WebDriver;
import selcrowler.runner.binding.Binding;

public interface ScriptRunner {
    public void callback(WebDriver driver, Binding binding) throws Exception;
}
