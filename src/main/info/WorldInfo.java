package main.info;

import main.config.Config;
import main.log.Logger;
import main.standard.entities.Roller;
import main.standard.entities.StandardEntity;
import main.standard.entities.StandardEntityURN;
import main.standard.entities.Track;
import main.standard.messages.Input;
import main.standard.messages.RollerMessageInput;
import main.standard.model.Action;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

public class WorldInfo {
    private Map<StandardEntityURN, Collection<StandardEntity>> storedTypes = new EnumMap(StandardEntityURN.class);
    private double paverSpeed;
    private double paverY;
    private double paverLY;
    private double paverRY;
    private double rollerSpeed;
    private Config config;
    private boolean reRollerBegin;
    //    private RollerMessageInput input;
    private Input input;

    public WorldInfo() {
        input = null;

    }

    public WorldInfo(Config config){
        this.config = config;
    }

    public void init(){
        this.rollerSpeed = Double.valueOf(this.config.getValue("Roller1-Speed"));
        this.paverSpeed = Double.valueOf(this.config.getValue("Paver-Speed"));
//        this.paverY = this.config.getIntValue("Paver-Y");
        this.paverLY = this.config.getIntValue("PaverL-Y");
        this.paverRY = this.config.getIntValue("PaverR-Y");
        this.reRollerBegin = false;
        for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.ROLLER)) {
            Roller roller = (Roller) standardEntity;
            roller.setAction(Action.FORWARD);
//            if (roller.getIndex()==2||roller.getIndex()==4){
//                roller.setAction(Action.STILL);
//            }else if (roller.getIndex()==1||roller.getIndex()==3){
//                roller.setAction(Action.FORWARD);
//            }
            if (roller.getTrack() == null) {
                if (roller.getIndex()==1||roller.getIndex()==2){
                    for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.LEFT_TRACK)) {
                        Track track = (Track) standardEntity1;
                        if (track.getIndex() == 1) {
                            roller.setTrack(track);
                            roller.setX((track.getX()+track.getY())/2);
                        }
                    }
                }else {
                    for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.RIGHT_TRACK)) {
                        Track track = (Track) standardEntity1;
                        if (track.getIndex() == 1) {
                            roller.setTrack(track);
                            roller.setX((track.getX()+track.getY())/2);
                        }
                    }
                }
            }
        }
//        System.out.println();
    }

    public void merge(Input input){
        this.input = input;
        if (input==null){
            return;
        }
//        if (this.paverY<this.getRoadLength()){
//            this.paverY += paverSpeed;
//        }
        if (this.paverRY<this.getRoadLength()){
            this.paverRY += paverSpeed;
        }
        if (this.paverLY < this.getRoadLength()){
            this.paverLY += paverSpeed;
        }
//        Action action = input.getAction();
        for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.ROLLER)){

            Roller roller = (Roller) standardEntity;
            Action action = input.getActionOfRoller(roller.getIndex());
            roller.setAction(action);
//            if (roller.getIndex()==1){
//                roller.setDirection(Double.valueOf(input.getDirection()));
            if (action==null){
                continue;
            }else if (action==Action.FORWARD){
//                roller.setY(roller.getY()+this.rollerSpeed);
            }else if (action==Action.BACKWARD){
                if (roller.getLastAction()==Action.FORWARD){
                    //得改。根据不同的车改。改完ok
                    if (roller.getIndex()==1||roller.getIndex()==3){
                        roller.getTrack().addBackupReForwardPoint(roller.getY());
                        roller.getTrack().addBackupBreakpoint(roller.getY());
                        roller.getTrack().addBackupReBreakpoint(roller.getY());
                        //保证复压的车的第一次前进结点
                        if (roller.getTrack().getRollingTimes()==0){
                            roller.getTrack().updateReForwardPoint();
                        }
                        roller.getTrack().addRollingTimes();
                        if (this.isReRollerBegin()==false&&roller.getTrack().getRollingTimes()>=4){
                            this.setReRollerBegin(true);
                        }
                    }else if (roller.getIndex()==2||roller.getIndex()==4){
                        roller.getTrack().addReRollingTimes();
                    }
//                        if (roller.getTrack().getRollingTimes()>1){
//                            roller.getTrack().updateBreakpoint();
//                        }
                }
//                roller.setY(roller.getY()-this.rollerSpeed);
            }else if (action==Action.RIGHT){
                if (roller.getLastAction()!=Action.RIGHT){
                    roller.updatePreTrackY();
                    //得改。后面的车要多几遍才更新.ok
                    if(roller.getIndex()==1||roller.getIndex()==3){
                        if (roller.getTrack().getRollingTimes()>1){
                            roller.getTrack().updateBreakpoint();
                        }
                    }else if (roller.getIndex()==2||roller.getIndex()==4){
                        roller.getTrack().updateReForwardPoint();
                        if (roller.getTrack().getReRollingTimes()>3&&roller.getTrack().getReRollingTimes()%2==0){
                            roller.getTrack().updateReBreakpoint();
                        }
                    }


                    int trackIndex = roller.getTrack().getIndex();
                    if (roller.getIndex()==1||roller.getIndex()==2){
                        for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.LEFT_TRACK)){
                            Track track = (Track) standardEntity1;
                            if (track.getIndex()==trackIndex+1){
                                roller.setTrack(track);
                                break;
                            }
                        }
                    }else if (roller.getIndex()==3||roller.getIndex()==4){
                        for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.RIGHT_TRACK)){
                            Track track = (Track) standardEntity1;
                            if (track.getIndex()==trackIndex+1){
                                roller.setTrack(track);
                                break;
                            }
                        }
                    }
                }

            }else if (action==Action.LEFT){
                if (roller.getLastAction()!=Action.LEFT){
                    roller.updatePreTrackY();
                    //得改。后面的车要多几遍才更新
                    if(roller.getIndex()==1||roller.getIndex()==3){
                        if (roller.getTrack().getRollingTimes()>1){
                            roller.getTrack().updateBreakpoint();
                        }
                    }else if (roller.getIndex()==2||roller.getIndex()==4){
                        roller.getTrack().updateReForwardPoint();
                        if (roller.getTrack().getReRollingTimes()>3&&roller.getTrack().getReRollingTimes()%2==0){
                            roller.getTrack().updateReBreakpoint();
                        }
                    }

                    int trackIndex = roller.getTrack().getIndex();
                    if (roller.getIndex()==1||roller.getIndex()==2){
                        for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.LEFT_TRACK)){
                            Track track = (Track) standardEntity1;
                            if (track.getIndex()==trackIndex-1){
                                roller.setTrack(track);
                                break;
                            }
                        }
                    }else if (roller.getIndex()==3||roller.getIndex()==4){
                        for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.RIGHT_TRACK)){
                            Track track = (Track) standardEntity1;
                            if (track.getIndex()==trackIndex-1){
                                roller.setTrack(track);
                                break;
                            }
                        }
                    }

                }
            } else {
                Logger.error("未知的动作");
            }
            if(roller.getIndex()==3){
                int i =1;
            }
            roller.setX(input.getPositionXOfRoller(roller.getIndex()));
            roller.setY(input.getPositionYOfRoller(roller.getIndex()));
        }

//        }
    }

//    public void merge(Action action){
//        if (this.paverY<this.getRoadLength()){
//            this.paverY += paverSpeed;
//        }
//        for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.ROLLER)){
//            Roller roller = (Roller) standardEntity;
//            roller.setAction(action);
//            if (action==null){
//                return;
//            }else if (action==Action.FORWARD){
//                roller.setY(roller.getY()+this.rollerSpeed);
//            }else if (action==Action.BACKWARD){
//                if (roller.getLastAction()==Action.FORWARD){
//                    roller.getTrack().addBackupBreakpoint(roller.getY());
//                    roller.getTrack().addRollingTimes();
//                }
//                roller.setY(roller.getY()-this.rollerSpeed);
//            }else if (action==Action.RIGHT){
//                if ((roller.getTrack().getRollingTimes()%2)==0 && roller.getTrack().getRollingTimes()!=0){
//                    roller.getTrack().updateBreakpoint();
//                }
//                int trackIndex = roller.getTrack().getIndex();
//                for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.TRACK)){
//                    Track track = (Track) standardEntity1;
//                    if (track.getIndex()==trackIndex+1){
//                        roller.setTrack(track);
//                        roller.setX(track.getX());
//                        break;
//                    }
//                }
//            }else if (action==Action.LEFT){
//                if (roller.getTrack().getRollingTimes()/2==0){
//                    roller.getTrack().updateBreakpoint();
//                }
//                int trackIndex = roller.getTrack().getIndex();
//                for (StandardEntity standardEntity1 : storedTypes.get(StandardEntityURN.TRACK)){
//                    Track track = (Track) standardEntity1;
//                    if (track.getIndex()==trackIndex-1){
//                        roller.setTrack(track);
//                        roller.setX(track.getX());
//                        break;
//                    }
//                }
//            } else {
//                Logger.error("未知的动作");
//            }
//
//        }
//    }

    public void addEntity(StandardEntity entity){

        if (this.storedTypes.keySet().contains(entity.getStandardURN())){
            this.storedTypes.get(entity.getStandardURN()).add(entity);
        }else {
            StandardEntityURN urn = entity.getStandardURN();
            Collection<StandardEntity> entities = new HashSet<>();
            entities.add(entity);
            this.storedTypes.put(urn,entities);
        }
    }

    public double getRollerSpeed() {
        return rollerSpeed;
    }

    public void setRollerSpeed(int rollerSpeed) {
        this.rollerSpeed = rollerSpeed;
    }

    public double getPaverSpeed(){
        return this.paverSpeed;
    }

    public double getPaverY(){
        return this.paverY;
    }
    public double getPaverLY(){
        return this.paverLY;
    }
    public double getPaverRY(){
        return this.paverRY;
    }
    public double getMaxDistanceFromRollerToPaver(){
        return Double.valueOf(this.config.getValue("MaxDistanceFromRollerToPaver"));
    }

    public int getLTrackNum(){
        return Integer.valueOf(this.config.getValue("LTrack-Num"));
    }
    public int getRTrackNum(){
        return Integer.valueOf(this.config.getValue("RTrack-Num"));
    }
    public double getRadius(){
        return Double.valueOf(this.config.getValue("Radius"));
    }
    public int getRoadLength(){
        return Integer.valueOf(this.config.getValue("RoadLength"));
    }
    public double getFixedValueOfRollerTurning(){
        return Double.valueOf(this.config.getValue("FixedValueOfRollerTurning"));
    }
    public int getRollerNum(){
        return Integer.valueOf(this.config.getValue("Roller-Num"));
    }
    public Collection<StandardEntity> getAllRoller(){
        return this.storedTypes.get(StandardEntityURN.ROLLER);
    }
    public Roller getRollerByIndex(int index){
        for (StandardEntity standardEntity :  this.storedTypes.get(StandardEntityURN.ROLLER)){
            Roller roller = (Roller) standardEntity;
            if (roller.getIndex()==index){
                return roller;
            }
        }
        Logger.error("未知的小车序号");
        return null;
    }
    public Track getLeftTrack(Track track){
        if (track.getIndex()<=1){
            Logger.error("错误，轨道超出范围");
        }
        Track leftTrack = null;
        if (track.isLTrack()){
            for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.LEFT_TRACK)){
                Track track1 = (Track) standardEntity;
                if (track1.getIndex() == track.getIndex()-1){
                    leftTrack = track1;
                    break;
                }
            }
        }else {
            for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.RIGHT_TRACK)){
                Track track1 = (Track) standardEntity;
                if (track1.getIndex() == track.getIndex()-1){
                    leftTrack = track1;
                    break;
                }
            }
        }

        return leftTrack;
    }

    public Track getRightTrack(Track track){
//        if (track.getIndex()>=this.getTrackNum()){
//            Logger.error("错误，轨道超出范围");
//        }
        Track rightTrack = null;
        if (track.isLTrack()){
            for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.LEFT_TRACK)){
                Track track1 = (Track) standardEntity;
                if (track1.getIndex() == track.getIndex()+1){
                    rightTrack = track1;
                    break;
                }
            }
        }else {
            for (StandardEntity standardEntity : storedTypes.get(StandardEntityURN.RIGHT_TRACK)){
                Track track1 = (Track) standardEntity;
                if (track1.getIndex() == track.getIndex()+1){
                    rightTrack = track1;
                    break;
                }
            }
        }

        return rightTrack;
    }

    public double getRollerInitialX(int index){
        String rollerX = "Roller"+index+"-X";
        return Double.valueOf(config.getValue(rollerX));
    }

    public double getRollerInitialY(int index){
        String rollerY = "Roller"+index+"-Y";
        return Double.valueOf(config.getValue(rollerY));
    }

    public boolean isReRollerBegin() {
        return reRollerBegin;
    }

    public void setReRollerBegin(boolean reRollerBegin) {
        this.reRollerBegin = reRollerBegin;
    }
}
