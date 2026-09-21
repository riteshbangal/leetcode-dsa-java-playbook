import java.util.ArrayDeque;
import java.util.Deque;

class EvaluateReversePolishNotation {
    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : tokens) {
            if (!isOperator(token)) {
                stack.push(Integer.parseInt(token));
                continue;
            }

            int rightOperand = stack.pop();
            int leftOperand = stack.pop();
            stack.push(calculate(leftOperand, rightOperand, token));
        }

        return stack.peek();
    }

    private boolean isOperator(String token) {
        return token.equals("+")
                || token.equals("-")
                || token.equals("*")
                || token.equals("/");
    }

    private int calculate(int leftOperand, int rightOperand, String operator) {
        switch (operator) {
            case "+":
                return leftOperand + rightOperand;
            case "-":
                return leftOperand - rightOperand;
            case "*":
                return leftOperand * rightOperand;
            case "/":
                return leftOperand / rightOperand;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}
