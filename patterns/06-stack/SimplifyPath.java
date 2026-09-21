import java.util.ArrayDeque;
import java.util.Deque;

class SimplifyPath {
    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();

        for (String component : path.split("/")) {
            if (component.isEmpty() || component.equals(".")) {
                continue;
            }

            if (component.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(component);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            result.append('/').append(stack.removeLast());
        }

        return result.length() == 0 ? "/" : result.toString();
    }
}
