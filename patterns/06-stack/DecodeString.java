import java.util.ArrayDeque;
import java.util.Deque;

class DecodeString {
    public String decodeString(String s) {
        Deque<String> stack = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);

            if (current != ']') {
                stack.push(String.valueOf(current));
                continue;
            }

            Deque<String> fragments = new ArrayDeque<>();

            while (!stack.peek().equals("[")) {
                fragments.push(stack.pop());
            }

            stack.pop();

            StringBuilder reversedDigits = new StringBuilder();

            while (!stack.isEmpty() && isSingleDigit(stack.peek())) {
                reversedDigits.append(stack.pop());
            }

            int repeatCount = Integer.parseInt(reversedDigits.reverse().toString());
            StringBuilder encodedText = new StringBuilder();

            while (!fragments.isEmpty()) {
                encodedText.append(fragments.pop());
            }

            StringBuilder decodedText = new StringBuilder();

            for (int repetition = 0; repetition < repeatCount; repetition++) {
                decodedText.append(encodedText);
            }

            stack.push(decodedText.toString());
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            result.append(stack.removeLast());
        }

        return result.toString();
    }

    private boolean isSingleDigit(String token) {
        return token.length() == 1 && Character.isDigit(token.charAt(0));
    }
}
