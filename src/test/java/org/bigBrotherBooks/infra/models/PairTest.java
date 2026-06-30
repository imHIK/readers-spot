package org.bigBrotherBooks.infra.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void equalityAndAccessors() {
        Pair<String, Integer> p1 = new Pair<>("a", 1);
        Pair<String, Integer> p2 = new Pair<>("a", 1);
        assertEquals("a", p1.getFirst());
        assertEquals(1, p1.getSecond());
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        p2.setSecond(2);
        assertNotEquals(p1, p2);
    }
}
