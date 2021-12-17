package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(4);

        boolean expected1 = true;
        boolean actual1 = arb.isEmpty();
        assertEquals(expected1, actual1);

        arb.enqueue(4);
        arb.enqueue(5);
        arb.enqueue(6);
        arb.enqueue(7);

        boolean expected2 = true;
        boolean actual2 = arb.isFull();
        assertEquals(expected2, actual2);

        int expected3 = 4;
        int actual3 = arb.peek();
        assertEquals(expected3, actual3);

        int expected4 = 4;
        int actual4 = arb.dequeue();
        assertEquals(expected4, actual4);

        arb.dequeue();
        arb.dequeue();
        arb.dequeue();

        boolean expected5 = true;
        boolean actual5 = arb.isEmpty();
        assertEquals(expected5, actual5);
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
