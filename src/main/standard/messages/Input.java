package main.standard.messages;

import main.info.WorldInfo;
import main.log.Logger;
import main.standard.model.Action;

import java.util.Collection;
import java.util.HashSet;

public class Input {
    private Collection<RollerMessageInput> rollerMessageInputs;


    public Input(WorldInfo worldInfo){
        this.rollerMessageInputs = new HashSet<>();
        for (int i = 1;i<=worldInfo.getRollerNum();i++){
            RollerMessageInput rollerMessageInput = new RollerMessageInput(i,worldInfo.getRollerInitialX(i),worldInfo.getRollerInitialY(i),"forward");
            this.rollerMessageInputs.add(rollerMessageInput);
        }
    }

    public RollerMessageInput getRollerMessageInput(int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex() == index){
                return rollerMessageInput;
            }
        }
        Logger.error("序号错误");
        return null;
    }

    public void setPositionXOfRoller(double x, int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex()==index){
                rollerMessageInput.setX(x);
                return;
            }
        }
        Logger.error("未知的roller序号");
    }

    public void setPositionYOfRoller(double y, int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex()==index){
                rollerMessageInput.setY(y);
                return;
            }
        }
        Logger.error("未知的roller序号");
    }

    public void setDirectionOfRoller(String direction, int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex()==index){
//                rollerMessageInput.setX(x);
                rollerMessageInput.setDirection(direction);
                return;

            }
        }
        Logger.error("未知的roller序号");
    }

    public double getPositionXOfRoller(int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex()==index){
                return rollerMessageInput.getX();
            }
        }
        Logger.error("未知的roller序号");
        return 0;
    }

    public double getPositionYOfRoller(int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex()==index){
                return rollerMessageInput.getY();
            }
        }
        Logger.error("未知的roller序号");
        return 0;
    }

    public Action getActionOfRoller(int index){
        for (RollerMessageInput rollerMessageInput : this.rollerMessageInputs){
            if (rollerMessageInput.getIndex()==index){
//                return rollerMessageInput.getY();
                return rollerMessageInput.getAction();
            }
        }
        Logger.error("未知的roller序号");
        return null;
    }
}
