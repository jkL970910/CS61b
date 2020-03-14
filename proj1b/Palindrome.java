public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        //s.charAt(int index) is used to get the index-th character in the string
        //s.length() is used to get the length of the string
        Deque<Character> string = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            string.addLast(word.charAt(i));
        }

        return string;
    }

    //recursive method
    private boolean isPalindrome(Deque<Character> wordInDeque) {
        while (wordInDeque.size() > 1) {
            return wordInDeque.removeFirst()
                    == wordInDeque.removeLast() && isPalindrome(wordInDeque);
        }
        return true;
    }

    public boolean isPalindrome(String word) {
        return isPalindrome(wordToDeque(word));
    }

    //To determine whether a word is a off-by-one Palindrome
    private boolean isPalindrome(Deque<Character> wordInDeque, CharacterComparator cc) {
        while (wordInDeque.size() > 1) {
            return cc.equalChars(wordInDeque.removeFirst(), wordInDeque.removeLast())
                    && isPalindrome(wordInDeque, cc);
        }

        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        return isPalindrome(wordToDeque(word), cc);
    }
}
