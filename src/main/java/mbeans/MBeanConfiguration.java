package mbeans;

import jakarta.annotation.*;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.*;
import java.lang.management.*;


@ApplicationScoped
public class MBeanConfiguration {
    private MBeanServer mBeanServer;
    private ObjectName pointCounterName;
    private ObjectName clickIntervalName;
    private PointCounter pointCounter;
    private ClickInterval clickInterval;

    @PostConstruct
    public void init() {
        try {
            mBeanServer = ManagementFactory.getPlatformMBeanServer();

            pointCounter = new PointCounter();
            pointCounterName = new ObjectName("mbeans:type=PointCounter");
            mBeanServer.registerMBean(pointCounter, pointCounterName);

            clickInterval = new ClickInterval();
            clickIntervalName = new ObjectName("mbeans:type=ClickInterval");
            mBeanServer.registerMBean(clickInterval, clickIntervalName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (pointCounterName != null) {
                mBeanServer.unregisterMBean(pointCounterName);
            }
            if (clickIntervalName != null) {
                mBeanServer.unregisterMBean(clickIntervalName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PointCounter getPointCounter() {
        return pointCounter;
    }

    public ClickInterval getClickInterval() {
        return clickInterval;
    }
}