public class TestArray {
    public static void main(String[] args) {
        testAddArray();
        testRemoveArray();
    }

    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static void testRemoveArray() {
        ArrayDeque<Integer> A = new ArrayDeque<>();
        boolean passed = checkEmpty(true, A.isEmpty());

        for (int i = 1; i < 10; i++) {
            A.addFirst(i);
            A.addLast(i);
        }
        A.printDeque();

        for (int i = 1; i < 9; i++) {
            A.removeFirst();
            A.removeLast();
            passed = checkSize(18 - 2 * i, A.size()) && passed;
            printTestStatus(passed);
        }
    }

    public static void testAddArray() {

        ArrayDeque<Integer> A = new ArrayDeque<>();
        boolean passed = checkEmpty(true, A.isEmpty());

        for (int i = 1; i < 10; i++) {
            A.addFirst(i);
            A.addLast(i);
            passed = checkSize(2 * i, A.size()) && passed;
            printTestStatus(passed);

            System.out.println("Printing out deque: ");
            A.printDeque();
        }
    }
}
