public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> stringDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            stringDeque.addLast(word.charAt(i));
        }
        return stringDeque;
    }

    private boolean isPalindromeHelper(Deque<Character> d) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        if (d.removeFirst() == d.removeLast()) {
            return isPalindromeHelper(d);
        }
        return false;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        return isPalindromeHelper(d);
    }

    private boolean isPalindrome2Helper(Deque<Character> d, CharacterComparator condition) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        if (condition.equalChars(d.removeFirst(), d.removeLast())) {
            return isPalindrome2Helper(d, condition);
        }
        return false;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        return isPalindrome2Helper(d, cc);
    }
}
