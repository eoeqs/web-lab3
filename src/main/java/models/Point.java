package models;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

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

    @NotNull
    @Column(name = "x", nullable = false)
    private double x;

    @NotNull
    @Column(name = "y", nullable = false)
    private double y;

    @NotNull
    @Column(name = "r", nullable = false)
    private double r;

    @NotNull
    @Column(name = "result", nullable = false)
    @Getter
    private boolean result;


}
