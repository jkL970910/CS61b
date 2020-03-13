import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    /*// You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.*/
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testisPalindrome() {
        boolean actual = palindrome.isPalindrome("abcdedcba");
        if (actual) {
            System.out.println("The word is Palindrome");
        } else {
            System.out.println("The word is not Palindrome");
        }
    }

    @Test
    public void testisPalindrome2() {
        OffByOne obo = new OffByOne();
        if((palindrome.isPalindrome("12345432", obo))) {
            System.out.println("The word is Off-by-One Palindrome");
        } else {
            System.out.println("The word is not Off-by-One Palindrome");
        }
    }
}
