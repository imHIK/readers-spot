package org.bigBrotherBooks.infra.models;

import java.util.HashMap;
import java.util.Map;

public class LFUCache<K, V> {
    private int capacity;
    private int minFrequency;
    private final Map<Integer, DoublyLinkedList<K>> freqMap;
    private final Map<K, Pair<V, Integer>> data;
    private final Map<K, Node<K>> reference;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        freqMap = new HashMap<>();

        data = new HashMap<>();
        reference = new HashMap<>();
        minFrequency = 0;
    }

    public V get(K key){
        if (!data.containsKey(key)){
            return null;
        }
        updateUse(key);
        return data.get(key).getFirst();
    }

    public void put(K key, V value){
        if (data.containsKey(key)) {
            data.get(key).setFirst(value);
            updateUse(key);
        } else {
            if (capacity == 0) {
                DoublyLinkedList<K> dll = freqMap.get(minFrequency);
                K lfuKey = dll.removeBack();
                data.remove(lfuKey);
                reference.remove(lfuKey);
            } else {
                capacity--;
            }
            DoublyLinkedList<K> dll = freqMap.computeIfAbsent(1, i -> new DoublyLinkedList<>());
            Node<K> node = new Node<>(key);
            dll.addFront(node);
            reference.put(key, node);
            data.put(key, new Pair<>(value, 1));
            minFrequency = 1;
        }
    }

    private void updateUse(K key) {
        int frequency = data.get(key).getSecond();
        DoublyLinkedList<K> dll = freqMap.get(frequency);
        Node<K> ref = reference.get(key);
        dll.remove(ref);
        if (frequency == minFrequency && dll.isEmpty()){
            minFrequency++;
        }
        DoublyLinkedList<K> newDll = freqMap.computeIfAbsent( frequency + 1, i -> new DoublyLinkedList<>());
        newDll.addFront(ref);
        data.get(key).setSecond(frequency+1);
    }
}
