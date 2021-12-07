package main.algorithm;

import main.info.WorldInfo;
import main.log.Logger;
import main.standard.entities.Road;
import main.standard.entities.Roller;
import main.standard.entities.StandardEntity;
import main.standard.messages.Output;
import main.standard.model.Action;
import main.standard.model.Lock;

import java.util.*;

public class SamplePathPlanning extends PathPlanning{
//    private Action result;

    private Output output;
    private Roller roller;
//    private Collection<Roller> rollers;
    private boolean flag;
    public SamplePathPlanning(WorldInfo worldInfo) {
        super(worldInfo);
//        this.result = null;
        this.output = new Output(this.worldInfo.getRollerNum());
        this.roller = null;
//        this.rollers = new HashSet<>();
        this.flag = true;
    }
    @Override
    public PathPlanning calc() {
//        double paverY = this.worldInfo.getPaverY();
        double paverLY = this.worldInfo.getPaverLY();
        double paverRY = this.worldInfo.getPaverRY();
        double maxDistanceFromRollerToPaver = this.worldInfo.getMaxDistanceFromRollerToPaver();
        Collection<StandardEntity> rollers = this.worldInfo.getAllRoller();

        for (StandardEntity standardEntity : rollers){
            Roller roller = (Roller) standardEntity;
            Action result = null;
//            this.rollers.add(roller);
            if ((roller.getIndex()==2||roller.getIndex()==4)&&this.worldInfo.isReRollerBegin()==false){
                continue;
            }
            if (roller.getAction()== Action.FORWARD){
                if (roller.getIndex()==1||roller.getIndex()==3){
                    if (roller.getIndex()==1){
                        if (paverLY-roller.getY()<=maxDistanceFromRollerToPaver || roller.getY()>=worldInfo.getRoadLength()){
                            result = Action.BACKWARD;
                        }else {
                            result = Action.FORWARD;
                        }
                    }else {
                        if (paverRY-roller.getY()<=maxDistanceFromRollerToPaver || roller.getY()>=worldInfo.getRoadLength()){
                            result = Action.BACKWARD;
                        }else {
                            result = Action.FORWARD;
                        }
                    }

                }else if (roller.getIndex()==2||roller.getIndex()==4){
                    if (Math.abs(roller.getY() - roller.getTrack().getReForwardPoint())<0.1||roller.getY()>=roller.getTrack().getReForwardPoint()){
                        result = Action.BACKWARD;
                    }else {
                        result = Action.FORWARD;
                    }
                }else {
                    Logger.error("未知的小车序号");
                }

            }else if(roller.getAction()==Action.BACKWARD){

                if (Math.abs(roller.getY()-roller.getTrack().getBreakpoint(roller))<0.1){
                    if (roller.getTrack().getIndex()==1){
                        if (roller.isRight()){
                            this.worldInfo.getLock().quit(roller);
                        }
                        result = Action.RIGHT;

                    }else if (((roller.isLeft())&&roller.getTrack().getIndex()==worldInfo.getLTrackNum())||((roller.isRight())&&
                            roller.getTrack().getIndex()==worldInfo.getRTrackNum())){
                        if (roller.isLeft()){
                            this.worldInfo.getLock().quit(roller);
                        }
                        result = Action.LEFT;
                    }else {
                        int newIndex = roller.getTrack().getIndex();
                        int oldIndex = roller.getTrackHistory().get(roller.getTrackHistory().size()-2).getIndex();
                        if (newIndex<oldIndex){
                            if (roller.isRight()){
                                Lock lock = this.worldInfo.getLock();
                                if (lock.isAllow(roller)){
                                    lock.access(roller);
                                    result = Action.LEFT;
                                }else {
                                    result = Action.PAUSE;
                                }
                            }else {
                                result = Action.LEFT;
                            }
                        }else if (newIndex>oldIndex){
                            if (roller.isLeft()){
                                Lock lock = this.worldInfo.getLock();
                                if (lock.isAllow(roller)){
                                    lock.access(roller);
                                    result = Action.RIGHT;
                                }else {
                                    result = Action.PAUSE;
                                }
                            }else {
                                result = Action.RIGHT;
                            }
                        }else {
                            Logger.error("轨道序号出错了");
                        }
                    }
                }else {
                    //继续后退
                    result = Action.BACKWARD;
                }

            }else if (roller.getAction()==Action.RIGHT){

                if (Math.abs(roller.getX()-roller.getTrack().getMidpoint())<0.01){
                    result = Action.FORWARD;
                }else {
                    result = Action.RIGHT;
                }
            }else if (roller.getAction()==Action.LEFT){
                if (Math.abs(roller.getX()-roller.getTrack().getMidpoint())<0.01){
                    result = Action.FORWARD;
                }else {
                    result = Action.LEFT;
                }
            } else {
                Logger.error("压路机无动作,错误");
                return this;
            }
            if (result!=Action.PAUSE){
                this.generateOutput(result, roller);
            }
//            System.out.println("位置:"+ roller.getX()+","+roller.getY()+"角度："+this.worldInfo.getInput().getDirection()+"  下一步动作为：" +result +"目标点："+this.output.getX()+","+this.output.getY()+
//                    "  摊铺机位置："+this.worldInfo.getPaverY());
        }

        return this;
    }


    @Override
    public Output getOutput() {
        return this.output;
    }

    private void generateOutput(Action result, Roller roller){
        if (result==Action.BACKWARD){
            this.output.setDirectionOfRoller("backward", roller.getIndex());
            this.output.setReversingOfRoller(true,roller.getIndex());
        }else {
            if (result==Action.FORWARD){
                this.output.setDirectionOfRoller("forward", roller.getIndex());
                this.output.setReversingOfRoller(false,roller.getIndex());
            }else if (result==Action.RIGHT){
                this.output.setDirectionOfRoller("right", roller.getIndex());
                this.output.setReversingOfRoller(false,roller.getIndex());
            }else if (result==Action.LEFT){
                this.output.setDirectionOfRoller("left", roller.getIndex());
                this.output.setReversingOfRoller(false,roller.getIndex());
            }
        }

        this.output.setPositionXOfRoller(this.generateOutputX(result, roller), roller.getIndex());
        this.output.setPositionYOfRoller(this.generateOutputY(result, roller), roller.getIndex());
    }

    private double generateOutputX(Action result, Roller roller){
        double x = roller.getX();
        if (result == Action.RIGHT){
            if (roller.getAction()!=Action.RIGHT){
                x = this.worldInfo.getRightTrack(roller.getTrack()).getMidpoint();
            }else {
                x = roller.getTrack().getMidpoint();
            }

        }else if (result == Action.LEFT){
            if (roller.getAction()!=Action.LEFT){
                x = this.worldInfo.getLeftTrack(roller.getTrack()).getMidpoint();
            }else {
                x = roller.getTrack().getMidpoint();
            }
        }
        return x;
    }

    private double generateOutputY(Action result, Roller roller){
        double y = roller.getY();
        if (result == Action.FORWARD){
            y+=this.worldInfo.getRollerSpeed();
        }else if (result == Action.BACKWARD){
            y-=this.worldInfo.getRollerSpeed();
            if (roller.getIndex()==1||roller.getIndex()==3){
                y = Math.max(y,roller.getTrack().getBreakpoint());
            }else if (roller.getIndex()==2||roller.getIndex()==4){
                y = Math.max(y,roller.getTrack().getReBreakpoint());
            }
        }else if (result == Action.RIGHT){
            if (roller.getAction()!=Action.RIGHT){
                y+=this.worldInfo.getFixedValueOfRollerTurning();
            }else {
                y=this.worldInfo.getFixedValueOfRollerTurning()+roller.getPreTrackY();
            }

        }else if (result == Action.LEFT){
            if (roller.getAction()!=Action.LEFT){
                y+=this.worldInfo.getFixedValueOfRollerTurning();
            }else {
                y=this.worldInfo.getFixedValueOfRollerTurning()+roller.getPreTrackY();
            }
        }
        return y;
    }
}
