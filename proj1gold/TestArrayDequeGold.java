import        static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

//@source StudentArrayDequeLauncher
public class TestArrayDequeGold {
    @Test
    public void testArrayDequeTask1() {
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();

        //test addLast
        for (int i = 0; i < 10; i++) {
            //create an int random number range from 0 to n
            int random = StdRandom.uniform(100);
            ads.addLast(random);
            sad.addLast(random);
        }
        for (int i = 0; i < 10; i++) {
            int actual = sad.get(i);
            int expected = ads.get(i);
            //assertEquals(String message, int expected, int actual)
            //if the compare returns true, the message won't be mentioned
            assertEquals("Oh noooo!\nThis is bad in addLast():\n   Random number " + actual
                    + " is not equal to " + expected + "!", expected, actual);
        }

        //test addFirst
        for (int i = 0; i < 10; i++) {
            //create an int random number range from 0 to n
            int random = StdRandom.uniform(100);
            ads.addFirst(random);
            sad.addFirst(random);
        }
        for (int i = 0; i < 10; i++) {
            int actual = sad.get(i);
            int expected = ads.get(i);
            //assertEquals(String message, int expected, int actual)
            //if the compare returns true, the message won't be mentioned
            assertEquals("Oh noooo!\nThis is bad in addFirst():\n   Random number " + actual
                    + " is not equal to " + expected + "!", expected, actual);
        }

        //test removeFirst
        List<Integer> actualList = new ArrayList<>();
        List<Integer> expectedList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            actualList.add(sad.removeFirst());
            expectedList.add(ads.removeFirst());
        }
        for (int i = 0; i < 10; i++) {
            int actual = sad.get(i);
            int expected = ads.get(i);
            assertEquals("Oh noooo!\nThis is bad in removeFirst():\n   Random number " + actual
                    + " not equal to " + expected + "!", expected, actual);
        }

        //test removeLast
        actualList.clear();
        expectedList.clear();
        for (int i = 0; i < 10; i++) {
            actualList.add(sad.removeLast());
            expectedList.add(ads.removeLast());
        }
        int actual = sad.size();
        int expected = ads.size();
        assertEquals("Oh noooo!\nThis is bad in removeLast():\n   Random number " + actual
                + " is not equal to " + expected + " !", expected, actual);
        for (int i = 0; i < 10; i++) {
            assertEquals("Oh noooo!\nThis is bad in removeLast():\n   Random number "
                    + actualList.get(i) + " is not equal to " + expectedList.get(i)
                    + "!", expectedList.get(i), actualList.get(i));
        }

    }
}
