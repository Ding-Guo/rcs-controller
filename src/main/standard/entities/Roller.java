package main.standard.entities;

import main.log.Logger;
import main.standard.model.Action;
import main.standard.model.Entity;
import main.standard.model.EntityID;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Roller extends StandardEntity{
    private int index;
    private double x;
    private double y;
    private double width;
    private Action action;
    private List<Action> actionHistory;
    private List<Track> trackHistory;
    private Track track;
    private double preTrackY;
    public double getDirection() {
        return direction;
    }
    private boolean isReRoller;
    public double getPreTrackY() {
        return preTrackY;
    }

    public boolean isReRoller() {
        return isReRoller;
    }

    public void setReRoller(boolean reRoller) {
        isReRoller = reRoller;
    }

    //    public void setPreTrackY(double preTrackY) {
//        this.preTrackY = preTrackY;
//    }
    public void updatePreTrackY(){
        this.preTrackY = this.y;
    }
    public void setDirection(double direction) {
        this.direction = direction;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private double direction;
    public Roller(EntityID id) {
        super(id);
        this.actionHistory = new LinkedList<>();
        this.trackHistory = new LinkedList<>();
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Action getAction() {
        return action;
    }
    public Action getLastAction(){
        if (this.actionHistory.size()==0||this.actionHistory.size()==1){
            Logger.error("没有上一次动作，只有"+this.actionHistory.size()+"个动作");
        }
        return this.actionHistory.get(this.actionHistory.size()-2);
    }
    public void setAction(Action action) {
        this.action = action;
        this.actionHistory.add(action);
    }

    public List<Action> getActionHistory(){
        return this.actionHistory;
    }

    public List<Track> getTrackHistory() {
        return trackHistory;
    }

    public void setTrackHistory(List<Track> trackHistory) {
        this.trackHistory = trackHistory;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
        this.trackHistory.add(track);
    }

    public int getTrackIndex(){
        return track.getIndex();
    }
    public StandardEntityURN getStandardURN() {
        return StandardEntityURN.ROLLER;
    }

    @Override
    public Entity copy() {
        return null;
    }
}
