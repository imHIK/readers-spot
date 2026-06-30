package org.bigBrotherBooks.infra.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {

    private static final String TEMPLATE_PATTERN = "^\\{\\{\\s*(.+?)\\s*\\}\\}$";

    public static String getTemplatedString(String value) {
        return "{{ " + value + " }}";
    }

    public static boolean isTemplate(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Pattern.compile(TEMPLATE_PATTERN).matcher(value).matches();
    }

    public static Object resolveTemplate(String template, Map<String, Object> inputs) {
        if (StringUtils.isEmpty(template) || CollectionUtils.isEmpty(inputs) || !isTemplate(template)) {
            return template;
        }
        Matcher matcher = Pattern.compile(TEMPLATE_PATTERN).matcher(template);
        if (matcher.matches()) {
            String path = matcher.group(1);
            return getValueFromPath(path, inputs);
        }
        return template;
    }

    public static Map<String, Object> resolveTemplate(Map<String, Object> map, Map<String, Object> inputs) {
        if (CollectionUtils.isEmpty(map) || CollectionUtils.isEmpty(inputs)) {
            return map;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String strValue) {
                Object resolvedValue = resolveTemplate(strValue, inputs);
                entry.setValue(resolvedValue);
            } else if (value instanceof Map<?, ?> nestedMap) {
                //noinspection unchecked
                Map<String, Object> resolvedNestedMap = resolveTemplate((Map<String, Object>) nestedMap, inputs);
                entry.setValue(resolvedNestedMap);
            } else if (value instanceof List<?> list) {
                //noinspection unchecked
                List<Object> resolvedList = resolveTemplate((List<Object>) list, inputs);
                entry.setValue(resolvedList);
            }
        }
        return map;
    }

    public static List<Object> resolveTemplate(List<Object> list, Map<String, Object> inputs) {
        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(inputs)) {
            return list;
        }
        List<Object> resolvedList = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof String itemStr) {
                resolvedList.add(resolveTemplate(itemStr, inputs));
            } else if (item instanceof Map<?, ?> itemMap) {
                //noinspection unchecked
                resolvedList.add(resolveTemplate((Map<String, Object>) itemMap, inputs));
            } else if (item instanceof List<?> itemList) {
                //noinspection unchecked
                resolvedList.add(resolveTemplate((List<Object>) itemList, inputs));
            } else {
                resolvedList.add(item);
            }
        }
        return resolvedList;
    }

    private static Object getValueFromPath(String path, Map<String, Object> inputs) {
        String[] keys = path.split("\\.");
        Object current = inputs;
        for (String key : keys) {
            if (current == null) {
                return null;
            }

            if (key.contains("[") && key.contains("]")) {

                String arrayKey = key.substring(0, key.indexOf('['));
                int index = Integer.parseInt(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
                current = ((Map<?, ?>) current).get(arrayKey);
                if (current instanceof List<?> list) {
                    if (index < 0 || index >= list.size()) {
                        return null;
                    }
                    current = list.get(index);
                } else {
                    return null;
                }
            } else if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
            } else {
                return null;

            }
        }
        return current;
    }
}
