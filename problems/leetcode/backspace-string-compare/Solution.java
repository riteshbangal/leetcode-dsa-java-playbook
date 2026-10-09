import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public boolean backspaceCompare(String s, String t) {
        if (processBackspace(s).equals(processBackspace(t))) {
            return true;
        }

        return false;
    }

    public String processBackspace(String str) {
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c != '#') {
                stack.push(c);
            } else if (!stack.isEmpty()) {
                stack.pop();
            }
        }

        StringBuilder ans = new StringBuilder();

        while (!stack.isEmpty()) {
            ans.append(stack.pop());
        }

        return ans.reverse().toString();
    }
}
