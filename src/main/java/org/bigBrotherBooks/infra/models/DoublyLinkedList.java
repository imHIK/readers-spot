package org.bigBrotherBooks.infra.models;

public class DoublyLinkedList<T> {
    private int size;
    private final Node<T> front;
    private final Node<T> back;

    public DoublyLinkedList() {
        this.front = new Node<>(null);
        this.back = new Node<>(null);
        front.setNext(back);
        back.setPrev(front);
    }

    public void addFront(T val) {
        Node<T> node = new Node<>(val);
        addFront(node);
    }

    public void addFront(Node<T> node) {
        addAfter(front, node);
    }

    public void addAfter(Node<T> target, T val) {
        Node<T> node = new Node(val);
        addAfter(target, node);
    }

    public void addAfter(Node<T> target, Node<T> node) {
        node.setNext(target.getNext());
        node.setPrev(target);
        target.getNext().setPrev(node);
        target.setNext(node);
        size++;
    }

    public void addBack(T value) {
        addBefore(back, value);
    }

    public void addBack(Node<T> value) {
        addBefore(back, value);
    }

    public void addBefore(Node<T> target, T value) {
        Node<T> node = new Node<>(value);
        addBefore(target, node);
    }

    public void addBefore(Node<T> target, Node<T> node) {
        node.setNext(target);
        node.setPrev(target.getPrev());
        target.getPrev().setNext(node);
        target.setPrev(node);
        size++;
    }

    public T remove(Node<T> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        node.setPrev(null);
        node.setNext(null);
        size--;
        return node.getValue();
    }

    public T removeFront() {
        Node<T> node = front.getNext();
        if (node == back) {
            throw new IndexOutOfBoundsException("Can not remove from empty list");
        }
        front.setNext(node.getNext());
        front.getNext().setPrev(front);
        node.setPrev(null);
        node.setNext(null);
        size--;
        return node.getValue();
    }

    public T removeBack() {
        Node<T> node = back.getPrev();
        if (node == front) {
            throw new IndexOutOfBoundsException("Can not remove from empty list");
        }
        back.setPrev(node.getPrev());
        back.getPrev().setNext(back);
        node.setNext(null);
        node.setPrev(null);
        size--;
        return node.getValue();
    }

    public int size(){
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
