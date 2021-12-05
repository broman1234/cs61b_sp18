/** Performs some basic Array list tests. */
public class ArrayDequeTest {

    /* Utility method for printing out size checks. */
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

    public static <T> boolean checkGet(T expected, T actual) {
        if (expected != actual) {
            System.out.println("get(int i) returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkResizing(int expected, int actual) {
        if (expected != actual) {
            System.out.println("Returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     *The \n means newline.
     */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed! \n");
        } else {
            System.out.println("Test failed! \n");
        }
    }

    /** Adds a few things to the list, checking size(), get(int i) and resizing are correct. */
    public static void addRemoveSizeGetResizingTest() {
        System.out.println("Running add/remove/Size/Get/Resizing test.");

        ArrayDeque<String> lla1 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, lla1.isEmpty());

        // passed = checkResizing(8, lla1.len()) && passed;

        lla1.addFirst("middle");
        lla1.addFirst("front");
        lla1.addLast("back");
        lla1.addLast("a");
        lla1.addLast("b");
        lla1.addFirst("c");
        lla1.addLast("d");
        lla1.addFirst("e");

        passed = checkSize(8, lla1.size()) && passed;

        // passed = checkResizing(16, lla1.len()) && passed;
        /*
        passed = checkResizing(15, lla1.addNextFirst) && passed;
        passed = checkResizing(8, lla1.addNextLast) && passed;
        passed = checkResizing(0, lla1.removeNextFirst) && passed;
        passed = checkResizing(7, lla1.removeNextLast) && passed;
         */

        lla1.addLast("f");
        lla1.addFirst("g");

        passed = checkSize(10, lla1.size()) && passed;

        passed = checkGet("front", lla1.get(7)) && passed;
        passed = checkGet("f", lla1.get(8)) && passed;
        passed = checkGet("g", lla1.get(15)) && passed;

        // passed = checkResizing(16, lla1.len()) && passed;

        System.out.println("Printing out deque: ");
        lla1.printDeque();
        /*
        passed = checkResizing(14, lla1.addNextFirst) && passed;
        passed = checkResizing(9, lla1.addNextLast) && passed;
        passed = checkResizing(15, lla1.removeNextFirst) && passed;
        passed = checkResizing(8, lla1.removeNextLast) && passed;
         */

        lla1.removeLast();
        lla1.removeLast();
        lla1.removeFirst();
        lla1.removeFirst();
        lla1.removeLast();
        lla1.removeFirst();
        lla1.removeLast();

        passed = checkSize(3, lla1.size()) && passed;

        // passed = checkResizing(8, lla1.len()) && passed;
        /*
        passed = checkResizing(7, lla1.addNextFirst) && passed;
        passed = checkResizing(3, lla1.addNextLast) && passed;
        passed = checkResizing(0, lla1.removeNextFirst) && passed;
        passed = checkResizing(2, lla1.removeNextLast) && passed;
         */

        printTestStatus(passed);
    }

    /** Adds some items, then removes the same amount of items, and ensures that dll is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        ArrayDeque<Integer> lla1 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, lla1.isEmpty());

        lla1.addFirst(10);
        lla1.addFirst(20);

        passed = checkSize(2, lla1.size()) && passed;

        int removedItem1 = lla1.removeFirst();
        int removedItem2 = lla1.removeLast();

        passed = checkEmpty(true, lla1.isEmpty()) && passed;

        // passed = checkResizing(8, lla1.len()) && passed;

        System.out.println("Printing out removed item: " + removedItem1 + " " + removedItem2);

        printTestStatus(passed);
    }

    /** Check resizing array deque */


    public static void main(String[] args) {
        System.out.println("Running tests. \n");
        addRemoveSizeGetResizingTest();
        addRemoveTest();
    }
}