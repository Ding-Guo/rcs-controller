package main.standard.messages;

public class RollerMessageOutput {
    private int index;
    private Boolean isReversing;
    private double x;
    private double y;
    private String direction;

    public RollerMessageOutput(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }

    public Boolean getReversing() {
        return isReversing;
    }

    public void setReversing(Boolean reversing) {
        isReversing = reversing;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }



}
