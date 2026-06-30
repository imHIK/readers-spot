package org.bigBrotherBooks.infra.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTest {

    @Test
    void isEmpty_collection() {
        assertTrue(CollectionUtils.isEmpty((List<?>) null));
        assertTrue(CollectionUtils.isEmpty(List.of()));
        assertFalse(CollectionUtils.isEmpty(List.of(1)));
    }

    @Test
    void isEmpty_array() {
        assertTrue(CollectionUtils.isEmpty((Object[]) null));
        assertTrue(CollectionUtils.isEmpty(new Object[0]));
        assertFalse(CollectionUtils.isEmpty(new Object[]{1}));
    }

    @Test
    void isEmpty_map() {
        assertTrue(CollectionUtils.isEmpty((Map<?, ?>) null));
        assertTrue(CollectionUtils.isEmpty(Map.of()));
        assertFalse(CollectionUtils.isEmpty(Map.of("k", "v")));
    }
}
