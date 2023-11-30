package db;

import checkers.AreaChecker;
import checkers.AreaCheckerQualifier;
import com.google.gson.Gson;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import models.Point;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SessionScoped
@Named("pointRepository")
public class PointRepository implements Serializable {
    private SessionFactory factory;
    private Session session;

    public PointRepository() {
        try {
            this.factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Point.class)
                    .buildSessionFactory();

            this.createSession();
        } catch (Exception e) {
            System.out.println("Exception during session factory init: " + e.getMessage());
        }
    }

    private void createSession() {
        this.session = factory.getCurrentSession();
    }

    private static final int LATEST_POINTS_COUNT = 10;
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    @AreaCheckerQualifier
    private AreaChecker areaCheck;

    public List<Point> getPointsList(int start, int count) {
        return entityManager.createQuery("select point from Point point", Point.class).setFirstResult(start).setMaxResults(count).getResultList();
    }

    public int getPointsCount() {
        return entityManager.createQuery("select count(*) from Point", Number.class).getSingleResult().intValue();
    }

    public List<Point> getLatestPointsList() {
        int pointsCount = getPointsCount();
        int firstResult = Math.max(pointsCount - LATEST_POINTS_COUNT, 0);
        return entityManager.createQuery("select point from Point point", Point.class).setFirstResult(firstResult).setMaxResults(LATEST_POINTS_COUNT).getResultList();
    }

    @Transactional
    public Point addPoint(Point point) {
        areaCheck.checkHit(point);
        entityManager.merge(point);
        entityManager.flush();
        return point;
    }

    @Transactional
    public void clearPoints() {
        entityManager.createQuery("delete from Point").executeUpdate();
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

//    public String getHit() {
//        return new Gson().toJson(getLatestPointsList().stream().map(Point::getResult).collect(Collectors.toList()));
//    }

//    public String getPointsCoordinates() {
//        return new Gson().toJson(
//                getLatestPointsList().stream().map(Point::getCoordinates).collect(Collectors.toList()));
//    }
}
