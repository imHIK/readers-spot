package org.bigBrotherBooks.infra.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void evictsLeastRecentlyUsed() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        assertEquals(1, cache.get("a"));   // 'a' now most-recently-used
        cache.put("c", 3);                 // should evict 'b'
        assertNull(cache.get("b"));
        assertEquals(1, cache.get("a"));
        assertEquals(3, cache.get("c"));
    }

    @Test
    void updatesExistingValue() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("a", 9);
        assertEquals(9, cache.get("a"));
    }

    @Test
    void missingKeyReturnsNull() {
        assertNull(new LRUCache<String, Integer>(1).get("nope"));
    }
}
