package checkers;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import mbeans.MBeanConfiguration;
import models.Point;

import java.io.Serializable;

@Named("areaCheck")
@SessionScoped
@AreaCheckerQualifier
public class AreaChecker implements Serializable {
    @Inject
    private MBeanConfiguration mBeanConfiguration;

    public void checkHit(Point point) {
        boolean hit = pointIsInArea(point);
        point.setResult(hit);
        point.setTimestamp(System.currentTimeMillis());
        mBeanConfiguration.getPointCounter().addPoint(hit);
        mBeanConfiguration.getClickInterval().addClickTimestamp(point.getTimestamp());
    }

    private boolean pointIsInArea(Point point) {
        var x = point.getX();
        var y = point.getY();
        var r = point.getR();
        return pointIsInRect(x, y, r) || pointIsInTriangle(x, y, r) || pointIsInSector(x, y, r);
    }

    private boolean pointIsInRect(double x, double y, double r) {
        return (x <= 0 && y >= 0) && (x >= -r && y <= r / 2);
    }

    private boolean pointIsInSector(double x, double y, double r) {
        return (x <= 0 && y <= 0) && (x * x + y * y <= r * r / 4);
    }

    private boolean pointIsInTriangle(double x, double y, double r) {
        return (x >= 0 && y <= 0) && (x <= r) && (y <= r / 2) && (x - 2 * y <= r);
    }
}
