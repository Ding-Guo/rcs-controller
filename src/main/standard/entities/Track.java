package main.standard.entities;

import main.standard.model.Entity;
import main.standard.model.EntityID;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Track extends StandardEntity{
    private int index;
    private double x;
    private double y;
    private int reRollingTimes;
    private int rollingTimes;
    private double reForwardPoint;
    private double breakpoint;
    private double reBreakpoint;
    private Queue<Double> backupReBreakpoint;
    private Queue<Double> backupBreakpoint;
    private Queue<Double> backupReForwardPoint;
    private boolean isLTrack;
    public Track(EntityID id) {
        super(id);
        this.breakpoint = 0;
        this.reBreakpoint = 0;
        this.backupBreakpoint = new LinkedList<Double>();
        this.backupReBreakpoint = new LinkedList<Double>();
        this.backupReForwardPoint = new LinkedList<Double>();
    }

    public int getIndex() {
        return index;
    }

    public void addBackupBreakpoint(double breakpoint){
        this.backupBreakpoint.offer(breakpoint);
    }

    public void updateBreakpoint(){
        this.breakpoint = this.backupBreakpoint.poll();
    }

    public double getBreakpoint() {
        return breakpoint;
    }
    public double getBreakpoint(Roller roller) {
        if (roller.isReRoller()){
            return reBreakpoint;
        }else {
            return breakpoint;
        }
//        return breakpoint;
    }
    public int getRollingTimes() {
        return rollingTimes;
    }

    public void addRollingTimes(){
        this.rollingTimes++;
    }

    public void addBackupReForwardPoint(double reForwardPoint){
        this.backupBreakpoint.offer(reForwardPoint);
    }

    public void updateReForwardPoint(){
        this.reForwardPoint = this.backupReForwardPoint.poll();
    }

    public double getReForwardPoint() {
        return this.reForwardPoint;
    }

    public void addBackupReBreakpoint(double reBreakpoint){
        this.backupReBreakpoint.offer(reBreakpoint);
    }

    public void updateReBreakpoint(){
        this.reBreakpoint = this.backupReBreakpoint.poll();
    }

    public double getReBreakpoint(){
        return this.reBreakpoint;
    }

    public int getReRollingTimes() {
        return this.reRollingTimes;
    }

    public void addReRollingTimes(){
        this.reRollingTimes++;
    }

    public void setRollingTimes(int rollingTimes) {
        this.rollingTimes = rollingTimes;
    }

    public void setBreakpoint(double breakpoint) {
        this.breakpoint = breakpoint;
    }

    public boolean isLTrack() {
        return isLTrack;
    }

    public void setLTrack(boolean LTrack) {
        isLTrack = LTrack;
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




    @Override
    public StandardEntityURN getStandardURN() {
        if (this.isLTrack){
            return StandardEntityURN.LEFT_TRACK;
        }else {
            return StandardEntityURN.RIGHT_TRACK;
        }
    }

    @Override
    public Entity copy() {
        return null;
    }
    public double getMidpoint(){
        return (x+y)/2;
    }
}
