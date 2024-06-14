package mbeans;

import java.util.LinkedList;
import java.util.Queue;

public class ClickInterval implements ClickIntervalMBean {
    private static final int MAX_INTERVALS = 100;
    private final Queue<Long> timestamps = new LinkedList<>();

    @Override
    public synchronized double getAverageInterval() {
        if (timestamps.size() < 2) return 0.0;

        long totalInterval = 0;
        Long previous = null;

        for (Long timestamp : timestamps) {
            if (previous != null) {
                totalInterval += (timestamp - previous);
            }
            previous = timestamp;
        }

        return (double) totalInterval / (timestamps.size() - 1);
    }

    @Override
    public synchronized void addClickTimestamp(long timestamp) {
        if (timestamps.size() == MAX_INTERVALS) {
            timestamps.poll();
        }
        timestamps.add(timestamp);
    }
}