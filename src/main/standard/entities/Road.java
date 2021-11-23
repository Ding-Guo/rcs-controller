package main.standard.entities;

import main.standard.model.Entity;
import main.standard.model.EntityID;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Road extends StandardEntity{
    private double x;
    private double y;
    private int rollingTimes;

    public Road(EntityID id) {
        super(id);
        rollingTimes=0;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getRollingTimes() {
        return rollingTimes;
    }

    public void setRollingTimes(int rollingTimes) {
        this.rollingTimes = rollingTimes;
    }

    public StandardEntityURN getStandardURN() {
        return StandardEntityURN.ROAD;
    }

    @Override
    public Entity copy() {
        return null;
    }
}
