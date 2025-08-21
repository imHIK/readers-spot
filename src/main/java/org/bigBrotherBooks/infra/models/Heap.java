package org.bigBrotherBooks.infra.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Heap {
    List<Integer> data;

    public Heap() {
        data = new ArrayList<>();
    }

    public int getMin() {
        if (data.isEmpty()) {
            throw new IndexOutOfBoundsException("Can not get from a empty heap");
        }
        return data.getFirst();
    }

    public void insert(int val) {
        data.add(val);
        int i = size()-1;
        while(i != 0){
            int parent = parent(i);
            if ( data.get(parent) <= data.get(i)) {
                break;
            }
            Collections.swap(data,i,parent);
            i = parent;
        }
    }

    public int extractMin() {
        if (data.isEmpty()) {
            throw new IndexOutOfBoundsException("Can not remove from a empty heap");
        }
        int value = data.getFirst();
        data.set(0, data.getLast());
        data.removeLast();
        heapify(0);
        return value;
    }

    public int size() {
        return data.size();
    }

    public static Heap build(List<Integer> arr){
        Heap heap = new Heap();
        heap.data = new ArrayList<>(arr);

        for (int i = heap.size()/2 ; i>=0 ; i--){
            heap.heapify(i);
        }
        return heap;
    }

    private void heapify(int i) {
        while (left(i) < size()) {
            int lc = left(i), rc = right(i), min = i;
            if (data.get(lc) < data.get(min)) {
                min = lc;
            }
            if (rc < size() && data.get(rc) < data.get(min)) {
                min = rc;
            }
            Collections.swap(data, i, min);
            i = min;
        }
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

}
