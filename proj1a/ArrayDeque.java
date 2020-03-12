public class ArrayDeque<T> {

    private int size;
    private double R;
    private int nextFirst;
    private int nextLast;
    private T[] items;
    
    /*Create an empty ArrayDeque*/
    
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
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
        R = (double)size / (double)items.length;
        if (items.length >= 16 && R < 0.25) {
            resize(items.length / 2);
        }
        if (isFull()) {
            resize(size * 2);
        }
    }

    public int size() {
        return size;
    }

    public void addFirst(T x) {
        adjustSize();
        items[nextFirst] = x;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    public void addLast(T x) {
        adjustSize();
        items[nextLast] = x;
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
