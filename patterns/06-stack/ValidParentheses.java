import java.util.ArrayDeque;
import java.util.Deque;

class ValidParentheses {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);

            if (isOpeningBracket(current)) {
                stack.push(current);
            } else {
                if (stack.isEmpty() || !isMatchingPair(stack.peek(), current)) {
                    return false;
                }

                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    private boolean isOpeningBracket(char bracket) {
        return bracket == '(' || bracket == '[' || bracket == '{';
    }

    private boolean isMatchingPair(char opening, char closing) {
        return (opening == '(' && closing == ')')
                || (opening == '[' && closing == ']')
                || (opening == '{' && closing == '}');
    }
}
