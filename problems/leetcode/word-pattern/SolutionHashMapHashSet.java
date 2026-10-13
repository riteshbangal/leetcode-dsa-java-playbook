import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class WordPatternHashMapHashSet {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");

        if (words.length != pattern.length()) {
            return false;
        }

        Map<Character, String> patternToWord = new HashMap<>();
        Set<String> usedWords = new HashSet<>();

        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];

            if (patternToWord.containsKey(c)) {
                if (!word.equals(patternToWord.get(c))) {
                    return false;
                }
            } else {
                if (usedWords.contains(word)) {
                    return false;
                }

                patternToWord.put(c, word);
                usedWords.add(word);
            }
        }

        return true;
    }
}
