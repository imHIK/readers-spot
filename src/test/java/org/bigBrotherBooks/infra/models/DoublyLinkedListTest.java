package org.bigBrotherBooks.infra.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    void addAndRemoveFromBothEnds() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        assertTrue(list.isEmpty());

        list.addFront(1);   // [1]
        list.addFront(2);   // [2,1]
        list.addBack(3);    // [2,1,3]
        assertEquals(3, list.size());

        assertEquals(2, list.removeFront());
        assertEquals(3, list.removeBack());
        assertEquals(1, list.removeFront());
        assertTrue(list.isEmpty());
    }

    @Test
    void removeFromEmptyThrows() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, list::removeFront);
        assertThrows(IndexOutOfBoundsException.class, list::removeBack);
    }

    @Test
    void removeNodeUnlinks() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        Node<String> b = new Node<>("b");
        list.addFront("a");
        list.addBack(b);
        list.addBack("c");      // [a, b, c]
        assertEquals("b", list.remove(b));
        assertEquals(2, list.size());
    }
}
