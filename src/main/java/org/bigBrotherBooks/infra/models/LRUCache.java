package org.bigBrotherBooks.infra.models;

import java.util.HashMap;
import java.util.LinkedList;

public class LRUCache<K, V> {
    private int capacity;
    private final HashMap<K, V> data;
    private final HashMap<K, Node<K>> reference;
    private final DoublyLinkedList<K> order;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        data = new HashMap<>();
        reference = new HashMap<>();
        order = new DoublyLinkedList<>();
    }

    public V get(K key) {
        if (!data.containsKey(key)) {
            return null;
        }
        updateUse(key);
        return data.get(key);
    }

    public void put(K key, V value) {
        if (data.containsKey(key)) {
            updateUse(key);
            data.put(key, value);
        } else {
            if (capacity == 0) {
                K lruKey = order.removeBack();
                reference.remove(lruKey);
                data.remove(lruKey);
            } else {
                capacity--;
            }
            data.put(key, value);
            Node<K> node = new Node<>(key);
            order.addFront(node);
            reference.put(key, node);
        }
    }

    private void updateUse(K key) {
        Node<K> node = reference.get(key);
        order.remove(node);
        order.addFront(node);
    }

}
