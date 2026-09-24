import java.util.Collections;
import java.util.PriorityQueue;

class LastStoneWeight {
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> maxHeap =
                new PriorityQueue<>(Collections.reverseOrder());

        for (int stone : stones) {
            maxHeap.offer(stone);
        }

        while (maxHeap.size() > 1) {
            int first = maxHeap.poll();
            int second = maxHeap.poll();
            int difference = first - second;

            if (difference > 0) {
                maxHeap.offer(difference);
            }
        }

        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }
}
