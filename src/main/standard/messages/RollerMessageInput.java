package main.standard.messages;

import main.log.Logger;
import main.standard.model.Action;

public class RollerMessageInput {

    private int index;
    private double x;
    private double y;
    private String direction;

    public RollerMessageInput(int index) {
        this.index = index;
    }

    public RollerMessageInput(int index, double x, double y, String direction) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
        if (direction.equals("forward")){
            return "90";
        }else if (direction.equals("right")){
            return "0";
        }else if (direction.equals("left")){
            return "180";
        }else if (direction.equals("backward")){
            return "270";
        }
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Action getAction(){

        Action action = null;
        if (direction.equals("forward")||isApproach(Double.valueOf(direction),90)){
            action = Action.FORWARD;
        }else if (direction.equals("backward")||isApproach(Double.valueOf(direction),270)){
            action = Action.BACKWARD;
        }else if (direction.equals("right")||(Double.valueOf(direction)<90&&Double.valueOf(direction)>0)){
            action = Action.RIGHT;
        }else if (direction.equals("left")||(Double.valueOf(direction)<180&&Double.valueOf(direction)>90)){
            action = Action.LEFT;
        }else {
            System.out.println("未知的方向:"+direction);
            Logger.error("未知的direction");
        }
        return action;
    }
    public boolean isApproach(Double x,int y){
        if (Math.abs(x-y)<1){
            return true;
        }else {
            return false;
        }
    }
}
