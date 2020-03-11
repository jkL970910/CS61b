public class ArrayDeque<T> {

    private int size;
    private int R;
    private int nextFirst;
    private int nextLast;
    private T[] items;
    
    /*Create an empty ArrayDeque*/
    
    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        nextFirst = 7;
        nextLast = 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isFull() {
        return size == items.length;
    }

    /*Add one circularly*/
    private int plusOne(int index) {
        return (index + 1) % items.length;
    }

    /*Minus one circularly*/
    private int minusOne(int index) {
        return (index - 1 + items.length) % items.length;
    }

    private void resize(int capacity) {
        T[] newDeque = (T[]) new Object[capacity];
        int oldIndex = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newDeque[i] = items[oldIndex];
            oldIndex = plusOne(oldIndex);
        }
        items = newDeque;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private void adjustSize() {
        R = size / items.length;
        if (R < 0.25) {
            resize(size / 2);
        }
        if (isFull()) {
            resize(size * R);
        }
    }

    public int size() {
        return size;
    }

    public void addFirst(T item) {
        adjustSize();
        items[nextFirst] = item;
        nextFirst = plusOne(nextFirst);
        size += 1;
    }

    public void addLast(T item) {
        adjustSize();
        items[nextLast] = item;
        nextLast = plusOne(nextLast);
        size += 1;
    }

    public void printDeque() {
        for (int i = plusOne(nextFirst); i != nextLast; i = plusOne(i)) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        adjustSize();
        nextFirst = plusOne(nextFirst);
        T toRemove = items[nextFirst];
        items[nextFirst] = null;
        if (!isEmpty()) {
            size -= 1;
        }

        return toRemove;
    }

    public T removeLast() {
        adjustSize();
        nextLast = minusOne(nextLast);
        T toRemove = items[nextLast];
        items[nextLast] = null;
        if (!isEmpty()) {
            size -= 1;
        }

        return toRemove;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int start = plusOne(nextFirst);
        return items[(start + index) % items.length];
    }

}
