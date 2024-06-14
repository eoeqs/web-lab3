package mbeans;

public interface ClickIntervalMBean {
    double getAverageInterval();

    void addClickTimestamp(long timestamp);
}