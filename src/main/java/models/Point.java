package models;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@ToString
@Named("point")
@SessionScoped
@Table(name = "points")
public class Point implements Serializable {

    public Point() {
    }
    public void setResult(boolean result) {
        this.result = result;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setR(double r) {
        this.r = r;
    }
    public double getX() {return x;}


    public double getY() {return y;}


    public double getR() {return r;}
    public boolean getResult() {
        return result;
    }

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id && Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0 && Double.compare(r, point.r) == 0 && result == point.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, r, result);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isResult() {
        return result;
    }
    public boolean isResult1() {
        return result;
    }
    public boolean isResult11() {
        return result;
    }

    @Column(name = "x", nullable = false)
    private double x;

    @Column(name = "y", nullable = false)
    private double y;

    @Column(name = "r", nullable = false)
    private double r;

    @Column(name = "result", nullable = false)
    private boolean result;


}
