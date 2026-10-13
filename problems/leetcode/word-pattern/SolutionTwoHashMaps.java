import java.util.HashMap;
import java.util.Map;

class WordPatternTwoHashMaps {
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");

        if (words.length != pattern.length()) {
            return false;
        }

        Map<Character, String> patternToWordMap = new HashMap<>();
        Map<String, Character> wordToPatternMap = new HashMap<>();

        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];

            if (patternToWordMap.containsKey(c)) {
                if (!word.equals(patternToWordMap.get(c))) {
                    return false;
                }
            } else if (wordToPatternMap.containsKey(word)) {
                if (c != wordToPatternMap.get(word)) {
                    return false;
                }
            } else {
                patternToWordMap.put(c, word);
                wordToPatternMap.put(word, c);
            }
        }

        return true;
    }
}
