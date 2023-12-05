package db;

import checkers.AreaChecker;
import checkers.AreaCheckerQualifier;
import com.google.gson.Gson;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import models.Point;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SessionScoped
@Named("pointRepository")
public class PointRepository implements Serializable {
    HibernateManager hibernateManager;

    public PointRepository() {
        hibernateManager = new HibernateManager();
    }

    @Inject
    @AreaCheckerQualifier
    private AreaChecker areaCheck;

    public List<Point> getPointsList(int start, int count) {
        return hibernateManager.getPointsList(start, count);

    }

    public int getPointsCount() {
        return hibernateManager.getPointsCount();
    }

    public List<Point> getLatestPointsList() {
        return hibernateManager.getLatestPointsList();
    }

    @Transactional
    public Point addPoint(Point point) {
        areaCheck.checkHit(point);
        hibernateManager.addPoint(point);
//        entityManager.merge(point);
//        entityManager.flush();
        return point;
    }

    @Transactional
    public void clearPoints() {
        hibernateManager.clearPoints();
    }

    @Transactional
    public void addPointFromJs() {
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        try {
            var x = Double.parseDouble(params.get("x"));
            var y = Double.parseDouble(params.get("y"));
            var graphR = Double.parseDouble(params.get("r"));

            final Point point = new Point(
                    x / graphR * graphR,
                    y / graphR * graphR,
                    graphR
            );
            addPoint(point);
            PrimeFaces.current().ajax().addCallbackParam("result", point.getResult());

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public String convertToJson(Function<? super Point, Double> getter) {
        return new Gson().toJson(getLatestPointsList().stream().map(getter).collect(Collectors.toList()));
    }

    public String getX() {
        return convertToJson(Point::getX);
    }

    public String getY() {
        return convertToJson(Point::getY);
    }

    public String getR() {
        return convertToJson(Point::getR);
    }

    public List<Point> getPoints() {
        return hibernateManager.getPoints();
    }

//    public String getHit() {
//        return new Gson().toJson(getLatestPointsList().stream().map(Point::getResult).collect(Collectors.toList()));
//    }

//    public String getPointsCoordinates() {
//        return new Gson().toJson(
//                getLatestPointsList().stream().map(Point::getCoordinates).collect(Collectors.toList()));
//    }
}
