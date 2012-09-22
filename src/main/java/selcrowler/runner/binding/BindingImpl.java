package selcrowler.runner.binding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BindingImpl implements Binding {

    private Map<Class, Map<String, Object>> bindingsMapByClass = new HashMap<Class, Map<String, Object>>();

    public <T> T get(Class<T> clazz) {
        Map<String, Object> classMap = bindingsMapByClass.get(clazz);
        if (classMap != null && classMap.size() > 0) {
            return (T)classMap.entrySet().iterator().next().getValue();
        }

        return null;
    }

    public <T> T get(Class<T> clazz, String name) {
        Map<String, Object> classMap = bindingsMapByClass.get(clazz);
        if (classMap != null && classMap.size() > 0) {
            return (T)classMap.get(name);
        }

        return null;
    }

    public <T> T get(String name) {
        return (T)getByName(name);
    }

    public <T> Set<T> getAll(Class<T> clazz) {
        Set<T> set = new HashSet<T>();
        for (Object value: bindingsMapByClass.get(clazz).values()) {
            set.add((T)value);
        }

        return set;
    }

    public void put(String name, Object value) {
        put(name, value, value.getClass());
    }

    public void put(String name, Object value, Class<?> valueClass) {
        if (value != null && !valueClass.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException();
        }

        removeByName(name);
        getOrCreateMap(valueClass).put(name, value);
    }

    public Binding add(String name, Object value) {
        this.put(name, value);
        return this;
    }

    public Binding add(String name, Object value, Class<?> valueClass) {
        this.put(name, value, valueClass);
        return this;
    }

    private Map<String, Object> getOrCreateMap(Class<?> valueClass) {
        Map<String, Object> map = bindingsMapByClass.get(valueClass);
        if (map == null) {
            map = new HashMap<String, Object>();
            bindingsMapByClass.put(valueClass, map);
        }

        return map;
    }

    private void removeByName(String name) {
        for (Map.Entry<Class, Map<String, Object>> entry: bindingsMapByClass.entrySet()) {
            Map<String, Object> map = entry.getValue();
            if (map != null) {
                map.remove(name);
            }
        }
    }

    private Object getByName(String name) {
        for (Map.Entry<Class, Map<String, Object>> entry: bindingsMapByClass.entrySet()) {
            Map<String, Object> map = entry.getValue();
            if (map != null) {
                return map.get(name);
            }
        }

        return null;
    }
}
