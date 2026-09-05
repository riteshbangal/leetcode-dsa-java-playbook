import java.util.HashMap;
import java.util.Map;

class FruitIntoBaskets {
    public int totalFruit(int[] fruits) {
        Map<Integer, Integer> frequencies = new HashMap<>();
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < fruits.length; right++) {
            int incoming = fruits[right];
            frequencies.put(incoming, frequencies.getOrDefault(incoming, 0) + 1);

            while (frequencies.size() > 2) {
                int outgoing = fruits[left];
                frequencies.put(outgoing, frequencies.get(outgoing) - 1);

                if (frequencies.get(outgoing) == 0) {
                    frequencies.remove(outgoing);
                }

                left++;
            }

            // The map describes a window with at most two distinct fruit types.
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}
