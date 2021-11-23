package main.registry;

import main.config.Config;
import main.info.WorldInfo;
import main.standard.entities.Road;
import main.standard.entities.Roller;
import main.standard.entities.StandardEntityURN;
import main.standard.entities.Track;
import main.standard.model.EntityID;

public class MultiWorldInitializer extends WorldInitializer{
    @Override
    public WorldInfo buildWorld(Config config) {
        WorldInfo worldInfo = new WorldInfo(config);
        this.createTrack(worldInfo,config);
        this.createRoller(worldInfo,config);
        return worldInfo;
    }

    private void createRoller(WorldInfo worldInfo,Config config){
        int numOfRoller = Integer.valueOf(config.getValue("Roller-Num"));

        for (int i=1;i<=numOfRoller;i++){
            Roller roller = new Roller(new EntityID(i));
            String rollerX = "Roller"+i+"-X";
            String rollerY = "Roller"+i+"-Y";
            String rollerWidth = "Roller"+i+"-Width";
            double x = Double.valueOf(config.getValue(rollerX));
            double y = Double.valueOf(config.getValue(rollerY));
            double width = Double.valueOf(config.getValue(rollerWidth));
            roller.setX(x+width/2);
            roller.setY(y);
            roller.setWidth(width);
            worldInfo.addEntity(roller);
        }
    }

    private void createTrack(WorldInfo worldInfo,Config config){
        int roadWidth = Integer.valueOf(config.getValue("RoadWidth"));
        int roadLength = Integer.valueOf(config.getValue("RoadLength"));
//        String rollerX = "Roller1-X";
        double rollerWidth = Double.valueOf(config.getValue("Roller1-Width"));
        for (int i=1;i<=roadWidth/rollerWidth;i++){
            Track track = new Track(new EntityID(i));
            track.setIndex(i);
            track.setX(i*rollerWidth/2);
            worldInfo.addEntity(track);
        }
    }
}
