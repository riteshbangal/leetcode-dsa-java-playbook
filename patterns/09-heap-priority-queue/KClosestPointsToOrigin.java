import java.util.PriorityQueue;

class KClosestPointsToOrigin {
    public int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap =
                new PriorityQueue<>(
                        (a, b) -> Long.compare(distanceSquared(b), distanceSquared(a))
                );

        for (int[] point : points) {
            maxHeap.offer(point);

            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        int[][] answer = new int[maxHeap.size()][];
        int index = 0;

        while (!maxHeap.isEmpty()) {
            answer[index++] = maxHeap.poll();
        }

        return answer;
    }

    private long distanceSquared(int[] point) {
        return 1L * point[0] * point[0]
                + 1L * point[1] * point[1];
    }
}
