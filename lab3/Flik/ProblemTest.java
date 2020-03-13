import static org.junit.Assert.*;

import org.junit.Test;

public class ProblemTest {

    @Test
    public void testFlik() {
        int a = 129;
        int b = 129;
        assertTrue(Flik.isSameNumber(a, b));
    }

    @Test
    public void testHorribleSteve() {
        int expected = 500;
        int actual = HorribleSteve.HorribleSteve();
        assertEquals(expected, actual);
    }
}