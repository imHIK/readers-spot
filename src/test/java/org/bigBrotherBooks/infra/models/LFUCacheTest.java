package org.bigBrotherBooks.infra.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LFUCacheTest {

    @Test
    void evictsLeastFrequentlyUsed() {
        LFUCache<String, Integer> cache = new LFUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        assertEquals(1, cache.get("a"));   // 'a' freq=2, 'b' freq=1
        cache.put("c", 3);                 // evicts 'b' (least frequent)
        assertNull(cache.get("b"));
        assertEquals(1, cache.get("a"));
        assertEquals(3, cache.get("c"));
    }

    @Test
    void missingKeyReturnsNull() {
        assertNull(new LFUCache<String, Integer>(1).get("nope"));
    }
}
