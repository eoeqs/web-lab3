package checkers;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import models.Point;

import java.io.Serializable;

@Named("areaCheck")
@SessionScoped
@AreaCheckerQualifier
public class AreaChecker implements Serializable {
    public void checkHit(Point point) {
        boolean hit = pointIsInArea(point);
        point.setResult(hit);
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
        return (x <= 0 && y <= 0) && (x*x + y*y <= r*r / 4);
    }

    private boolean pointIsInTriangle(double x, double y, double r) {
        return (x >= 0 && y <= 0) && (x <= r) && (y <= r/2) && (x - 2*y <= r);
    }

    private void pointIsIn(double x, double y, double r) {
        return;
    }

    private void pointIsIn1(double x, double y, double r) {
        return;
    }

}
