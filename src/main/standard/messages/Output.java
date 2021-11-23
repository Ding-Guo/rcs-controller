package main.standard.messages;

import main.log.Logger;

import java.util.Collection;
import java.util.HashSet;

public class Output {
    private Collection<RollerMessageOutput> rollerMessageOutputs;

    public Output(int rollerNum) {
        this.rollerMessageOutputs = new HashSet<>();
        for (int i=1;i<=rollerNum;i++){
            RollerMessageOutput rollerMessageOutput = new RollerMessageOutput(i);
            rollerMessageOutputs.add(rollerMessageOutput);
        }
    }
    public RollerMessageOutput getRollerMessageOutputByIndex(int index){
        for (RollerMessageOutput rollerMessageOutput : rollerMessageOutputs){
            if (rollerMessageOutput.getIndex() == index){
                return rollerMessageOutput;
            }
        }
        Logger.error("未知的roller序号");
        return null;
    }

    public void setPositionXOfRoller(double x, int index){
        for (RollerMessageOutput rollerMessageOutput : rollerMessageOutputs){
            if (rollerMessageOutput.getIndex()==index){
                rollerMessageOutput.setX(x);
                return;
            }
        }
        Logger.error("未知的roller序号");
    }

    public void setPositionYOfRoller(double y, int index){
        for (RollerMessageOutput rollerMessageOutput : rollerMessageOutputs){
            if (rollerMessageOutput.getIndex()==index){
                rollerMessageOutput.setY(y);
                return;
            }
        }
        Logger.error("未知的roller序号");
    }

    public void setDirectionOfRoller(String direction, int index){
        for (RollerMessageOutput rollerMessageOutput : rollerMessageOutputs){
            if (rollerMessageOutput.getIndex()==index){
                rollerMessageOutput.setDirection(direction);
                return;
            }
        }
        Logger.error("未知的roller序号");
    }

    public void setReversingOfRoller(boolean isReversing, int index){
        for (RollerMessageOutput rollerMessageOutput : rollerMessageOutputs){
            if (rollerMessageOutput.getIndex()==index){
                rollerMessageOutput.setReversing(isReversing);
                return;
            }
        }
        Logger.error("未知的roller序号");
    }
}
