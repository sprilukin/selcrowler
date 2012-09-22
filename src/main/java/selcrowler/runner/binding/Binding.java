package selcrowler.runner.binding;

import java.util.Set;

public interface Binding {

    public <T> T get(Class<T> clazz);

    public <T> T get(Class<T> clazz, String name);

    public <T> T get(String name);

    public <T> Set<T> getAll(Class<T> clazz);

    public void put(String name, Object value);

    public void put(String name, Object value, Class<?> valueClass);

    public Binding add(String name, Object value);

    public Binding add(String name, Object value, Class<?> valueClass);
}
