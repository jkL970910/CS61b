import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static CharacterComparator offBy4 = new OffByN(4);

    @Test
    public void testEqualChars() {
        assertTrue(offBy4.equalChars('a','e'));
        assertTrue(offBy4.equalChars('A','E'));
        assertTrue(offBy4.equalChars('e','a'));
        assertTrue(offBy4.equalChars('1','5'));
        assertTrue(offBy4.equalChars('5','1'));
    }

}