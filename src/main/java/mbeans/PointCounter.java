package mbeans;


import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMBean {
    private int totalPoints = 0;
    private int pointsInArea = 0;
    private int consecutiveMisses = 0;
    private long sequenceNumber = 0;

    @Override
    public synchronized int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public synchronized int getPointsInArea() {
        return pointsInArea;
    }

    @Override
    public synchronized void addPoint(boolean isInArea) {
        totalPoints++;
        if (isInArea) {
            pointsInArea++;
            consecutiveMisses = 0;
        } else {
            consecutiveMisses++;
            if (consecutiveMisses >= 2) {
                Notification notification = new Notification(
                        "missedTwice",
                        this,
                        sequenceNumber++,
                        System.currentTimeMillis(),
                        "User missed the area twice consecutively."
                );
                sendNotification(notification);
                consecutiveMisses = 0;
            }
        }
    }
}