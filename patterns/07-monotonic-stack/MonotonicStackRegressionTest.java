import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

class MonotonicStackRegressionTest {
    private static String mode;
    private static Constructor<?> constructor;
    private static Method method;
    private static int checks;

    public static void main(String[] args) throws Exception {
        mode = args[0];
        String type;
        String name;
        switch (mode) {
            case "greater": type = "NextGreaterElementRight"; name = "nextGreaterElements"; break;
            case "smaller": type = "NextSmallerElementRight"; name = "nextSmallerIndices"; break;
            case "previous": type = "PreviousSmallerElementLeft"; name = "previousSmallerIndices"; break;
            case "span": type = "StockSpan"; name = "calculateSpans"; break;
            case "daily": type = "Solution"; name = "dailyTemperatures"; break;
            case "circular": type = "Solution"; name = "nextGreaterElements"; break;
            case "mapped": type = "Solution"; name = "nextGreaterElement"; break;
            case "online": type = "StockSpanner"; name = "next"; break;
            default: throw new IllegalArgumentException(mode);
        }
        Class<?> solution = Class.forName(type);
        constructor = solution.getDeclaredConstructor();
        if (mode.equals("mapped")) {
            method = solution.getMethod(name, int[].class, int[].class);
            checkMapped(new int[] {4, 1, 2}, new int[] {1, 3, 4, 2});
            checkMapped(new int[] {2, 4}, new int[] {1, 2, 3, 4});
            for (int n = 0; n <= 6; n++) {
                int[] values = new int[n];
                for (int i = 0; i < n; i++) values[i] = i * 3 + 2;
                permutations(values, 0);
            }
        } else {
            method = solution.getMethod(name, mode.equals("online") ? int.class : int[].class);
            fixedExamples();
            for (int n = 0; n <= 6; n++) enumerate(new int[n], 0);
            if (mode.equals("online")) {
                Object a = constructor.newInstance();
                Object b = constructor.newInstance();
                checkCall(a, 100, 1);
                checkCall(b, 10, 1);
                checkCall(a, 80, 1);
                checkCall(b, 10, 2);
                checkCall(a, 120, 3);
            }
        }
        System.out.println(mode + ": Passed " + checks + " checks.");
    }

    private static void fixedExamples() throws Exception {
        switch (mode) {
            case "greater":
                check(new int[] {2, 1, 2, 4, 3}, new int[] {4, 2, 4, -1, -1});
                check(new int[] {-3, -2, 0}, new int[] {-2, 0, -1});
                break;
            case "smaller":
                check(new int[] {4, 2, 2, 5, 1}, new int[] {1, 4, 4, 4, -1});
                break;
            case "previous":
                check(new int[] {4, 2, 2, 5, 1}, new int[] {-1, -1, -1, 2, -1});
                break;
            case "daily":
                check(new int[] {73, 74, 75, 71, 69, 72, 76, 73},
                        new int[] {1, 1, 4, 2, 1, 1, 0, 0});
                check(new int[] {70, 70, 71}, new int[] {2, 1, 0});
                break;
            case "circular":
                check(new int[] {3, 1, 2}, new int[] {-1, 2, 3});
                check(new int[] {1, 2, 1}, new int[] {2, -1, 2});
                break;
            default:
                check(new int[] {100, 80, 60, 70, 60, 75, 85},
                        new int[] {1, 1, 1, 2, 1, 4, 6});
                check(new int[] {50, 50, 50}, new int[] {1, 2, 3});
                check(new int[] {100, 80, 60, 70, 60, 60, 75, 85},
                        new int[] {1, 1, 1, 2, 1, 2, 5, 7});
        }
    }

    private static void enumerate(int[] values, int index) throws Exception {
        if (index == values.length) {
            check(values, bruteForce(values));
            return;
        }
        int base = mode.equals("daily") ? 30
                : (mode.equals("span") || mode.equals("online") ? 50 : -1);
        for (int value = base; value < base + 3; value++) {
            values[index] = value;
            enumerate(values, index + 1);
        }
    }

    private static int[] bruteForce(int[] values) {
        int n = values.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            if (mode.equals("span") || mode.equals("online")) {
                int count = 1;
                for (int j = i - 1; j >= 0 && values[j] <= values[i]; j--) count++;
                result[i] = count;
            } else if (mode.equals("previous")) {
                result[i] = -1;
                for (int j = i - 1; j >= 0; j--) {
                    if (values[j] < values[i]) { result[i] = j; break; }
                }
            } else {
                result[i] = mode.equals("daily") ? 0 : -1;
                int limit = mode.equals("circular") ? n - 1 : n - i - 1;
                for (int distance = 1; distance <= limit; distance++) {
                    int j = (i + distance) % n;
                    boolean matches = mode.equals("smaller")
                            ? values[j] < values[i] : values[j] > values[i];
                    if (matches) {
                        result[i] = mode.equals("smaller") ? j
                                : (mode.equals("daily") ? distance : values[j]);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static void check(int[] values, int[] expected) throws Exception {
        int[] before = values.clone();
        Object instance = constructor.newInstance();
        int[] actual;
        if (mode.equals("online")) {
            actual = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                actual[i] = (Integer) method.invoke(instance, values[i]);
            }
        } else {
            actual = (int[]) method.invoke(instance, (Object) values);
        }
        assertArray(expected, actual);
        assertArray(before, values);
        checks++;
    }

    private static void permutations(int[] values, int index) throws Exception {
        if (index == values.length) {
            int[] queries = new int[values.length];
            for (int i = 0; i < values.length; i++) queries[i] = values[values.length - i - 1];
            checkMapped(queries, values);
            checkMapped(Arrays.copyOf(queries, (queries.length + 1) / 2), values);
            return;
        }
        for (int i = index; i < values.length; i++) {
            int temporary = values[index]; values[index] = values[i]; values[i] = temporary;
            permutations(values, index + 1);
            temporary = values[index]; values[index] = values[i]; values[i] = temporary;
        }
    }

    private static void checkMapped(int[] queries, int[] values) throws Exception {
        int[] originalQueries = queries.clone();
        int[] originalValues = values.clone();
        int[] expected = new int[queries.length];
        Arrays.fill(expected, -1);
        for (int q = 0; q < queries.length; q++) {
            int start = 0;
            while (start < values.length && values[start] != queries[q]) start++;
            if (start == values.length) throw new AssertionError("Invalid query fixture");
            for (int j = start + 1; j < values.length; j++) {
                if (values[j] > queries[q]) { expected[q] = values[j]; break; }
            }
        }
        int[] actual = (int[]) method.invoke(constructor.newInstance(), queries, values);
        assertArray(expected, actual);
        assertArray(originalQueries, queries);
        assertArray(originalValues, values);
        checks++;
    }

    private static void checkCall(Object instance, int price, int expected) throws Exception {
        int actual = (Integer) method.invoke(instance, price);
        if (actual != expected) throw new AssertionError("Online state was not isolated");
        checks++;
    }

    private static void assertArray(int[] expected, int[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(actual));
        }
    }
}
