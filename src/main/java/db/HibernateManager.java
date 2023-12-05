package db;

import models.Point;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;


public class HibernateManager {
    private final static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public HibernateManager() {
    }

    private static final int LATEST_POINTS_COUNT = 10;

    public List<Point> getPoints() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Point", Point.class).list();
        } catch (Exception _ignored) {
            return new ArrayList<>();
        }
    }

    public List<Point> getPointsList(int start, int count) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select point from Point point", Point.class).setFirstResult(start).setMaxResults(count).getResultList();
        }
    }

    public int getPointsCount() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select count(*) from Point", Number.class).getSingleResult().intValue();
        }
    }

    public List<Point> getLatestPointsList() {
        int pointsCount = getPointsCount();
        int firstResult = Math.max(pointsCount - LATEST_POINTS_COUNT, 0);
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select point from Point point", Point.class).setFirstResult(firstResult).setMaxResults(LATEST_POINTS_COUNT).getResultList();
        }
    }

    public void addPoint(Point point) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(point);
            transaction.commit();
        }
    }


    public void clearPoints() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Point").executeUpdate();
            transaction.commit();
        }
    }

}