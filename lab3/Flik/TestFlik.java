import org.junit.Test;
import static org.junit.Assert.*;

public class TestFlik {
    @Test
    public void testFlik() {
        int inputA = 19;
        int inputB = 19;
        assertTrue(Flik.isSameNumber(inputA, inputB));

        inputA = 1;
        inputB = 2;
        assertFalse(Flik.isSameNumber(inputA, inputB));
    }
}