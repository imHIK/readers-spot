package org.bigBrotherBooks.infra.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeapTest {

    @Test
    void insertAndExtractMaintainsMinOrder() {
        Heap heap = new Heap();
        heap.insert(5);
        heap.insert(3);
        heap.insert(8);
        heap.insert(1);
        assertEquals(1, heap.getMin());
        assertEquals(1, heap.extractMin());
        assertEquals(3, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(8, heap.extractMin());
        assertEquals(0, heap.size());
    }

    @Test
    void buildHeapifiesArbitraryList() {
        Heap heap = Heap.build(List.of(9, 4, 7, 1, 2, 6));
        assertEquals(1, heap.extractMin());
        assertEquals(2, heap.extractMin());
    }

    @Test
    void extractFromEmptyThrows() {
        assertThrows(IndexOutOfBoundsException.class, new Heap()::extractMin);
    }
}
