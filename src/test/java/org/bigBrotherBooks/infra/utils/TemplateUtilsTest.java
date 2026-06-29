package org.bigBrotherBooks.infra.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateUtilsTest {

    @Test
    void isTemplate_detectsDoubleBraces() {
        assertTrue(TemplateUtils.isTemplate("{{ name }}"));
        assertTrue(TemplateUtils.isTemplate("{{name}}"));
        assertFalse(TemplateUtils.isTemplate("name"));
        assertFalse(TemplateUtils.isTemplate(""));
        assertFalse(TemplateUtils.isTemplate(null));
    }

    @Test
    void resolveTemplate_resolvesSimpleKey() {
        Object resolved = TemplateUtils.resolveTemplate("{{ name }}", Map.of("name", "Alice"));
        assertEquals("Alice", resolved);
    }

    @Test
    void resolveTemplate_resolvesNestedPath() {
        Map<String, Object> inputs = Map.of("user", Map.of("city", "Pune"));
        assertEquals("Pune", TemplateUtils.resolveTemplate("{{ user.city }}", inputs));
    }

    @Test
    void resolveTemplate_resolvesArrayIndex() {
        Map<String, Object> inputs = Map.of("items", List.of("a", "b", "c"));
        assertEquals("b", TemplateUtils.resolveTemplate("{{ items[1] }}", inputs));
    }

    @Test
    void resolveTemplate_nonTemplateReturnedAsIs() {
        assertEquals("plain", TemplateUtils.resolveTemplate("plain", Map.of("a", "b")));
    }

    @Test
    void resolveTemplate_replacesValuesInMutableMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("greeting", "{{ name }}");
        Map<String, Object> result = TemplateUtils.resolveTemplate(map, Map.of("name", "Bob"));
        assertEquals("Bob", result.get("greeting"));
    }
}
