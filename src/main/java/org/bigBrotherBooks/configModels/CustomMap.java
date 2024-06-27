package org.bigBrotherBooks.configModels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomMap {
    Map<String, Object> map;

    public CustomMap() {
        map = new HashMap<>();
    }

    public CustomMap(List<Object> values) {
        map = new HashMap<>();
        values.forEach(value -> {
            put(value);
        });
    }

    public static CustomMap of(Object... values) {
        CustomMap map = new CustomMap();
        for (Object value : values) {
            map.put(value);
        }
        return map;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void put(Object value) {
        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                map.put(list.getFirst().getClass().getSimpleName(), value);
            }
        } else if (value instanceof Set) {
            if (((Set<?>) value).isEmpty()) {
                return;
            }
            map.put(((Set<?>) value).iterator().next().getClass().getSimpleName(), value);
        } else {
            map.put(value.getClass().getSimpleName(), value);
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
