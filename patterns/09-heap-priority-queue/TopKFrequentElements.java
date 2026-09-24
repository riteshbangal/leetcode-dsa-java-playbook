import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class TopKFrequentElements {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequency = new HashMap<>();

        for (int num : nums) {
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);
        }

        PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
                new PriorityQueue<>(
                        (a, b) -> Integer.compare(a.getValue(), b.getValue())
                );

        for (Map.Entry<Integer, Integer> candidate : frequency.entrySet()) {
            minHeap.offer(candidate);

            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        int[] answer = new int[minHeap.size()];
        int index = 0;

        while (!minHeap.isEmpty()) {
            answer[index++] = minHeap.poll().getKey();
        }

        return answer;
    }
}
