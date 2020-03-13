import java.util.LinkedList;

public class LinkedListDeque<T> implements Deque<T> {

    private class TNode {
        private TNode prev;
        private T item;
        private TNode next;

        private TNode(TNode p, T i, TNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private TNode sentinel;
    private int size;

    /*
    * Create an empty deque
    */
    public LinkedListDeque() {
        sentinel = new TNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /*
    * Adds an item of type Typename to the front
    * of the deque
    */
    @Override
    public void addFirst(T x) {
        sentinel.next = new TNode(sentinel, x, sentinel.next); //此处记住是右边先赋值后再赋值给左边
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /*
    * Add an item of type Typename to the back
    * of the deque
    */
    @Override
    public void addLast(T x) {
        sentinel.prev = new TNode(sentinel.prev, x, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /*
    * Returns true if deque is empty, false otherwise
    */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /*
    Returns the number of items in the deque
    */
    @Override
    public int size() {
        return size;

    }

    /*
    Removes and returns the item at the front of the deque.
    If no such item exists, return null
    */
    @Override
    public T removeFirst() {
        T removeItem = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        if (!isEmpty()) {
            size -= 1;
        }

        return removeItem;
    }

    /*
    Removes and returns the item at the last of the deque.
    If no such item exists, return null
    */
    @Override
    public T removeLast() {
        T removeItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        if (!isEmpty()) {
            size -= 1;
        }

        return removeItem;
    }

    /*
    Prints the items in the deque from first to last, separated by a space
    */
    @Override
    public void printDeque() { //创建一个指向sentinel.next的list，取出item后即指向next
        TNode toPrint = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(toPrint.item + " ");
            toPrint = toPrint.next;
        }
        System.out.println();
    }

    /*
    Gets the item at the given index, where 0 is the front,
    1 is the next item and so forth. If no such item exists,
    returns null. Must not alter the deque!
    */
    @Override
    public T get(int index) {
        TNode getItem = sentinel.next;
        for (int i = 0; i < index; i++) {
            getItem = getItem.next;
        }
        return getItem.item;
    }

    /*
    The same as get(), but uses recursion
    */
    private T getRecursiveHelper(int index, TNode getItemR) {
        if (index == 0) {
            return getItemR.item;
        } else {
            return getRecursiveHelper(index - 1, getItemR.next);
        }
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(index, sentinel.next);
    }

}
