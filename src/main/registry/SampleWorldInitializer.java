package main.registry;

import main.config.Config;
import main.info.WorldInfo;
import main.standard.entities.Road;
import main.standard.entities.Roller;
import main.standard.entities.StandardEntityURN;
import main.standard.entities.Track;
import main.standard.model.EntityID;

import java.util.Collection;

public class SampleWorldInitializer extends WorldInitializer{

    @Override
    public WorldInfo buildWorld(Config config) {
        WorldInfo worldInfo = new WorldInfo(config);
//        worldInfo.init();

//        this.createRoad(worldInfo,config);
        this.createTrack(worldInfo,config);
        this.createRoller(worldInfo,config);
        return worldInfo;
    }

    private void createRoad(WorldInfo worldInfo,Config config){
        int roadWidth = Integer.valueOf(config.getValue("RoadWidth"));
        int roadLength = Integer.valueOf(config.getValue("RoadLength"));
        int maximumIntervalOfWidth = Integer.valueOf(config.getValue("MaximumIntervalOfWidth"));
        int maximumIntervalOfLength = Integer.valueOf(config.getValue("MaximumIntervalOfLength"));
        for (int i=1;i<roadWidth;i=i+maximumIntervalOfWidth){
            for (int j=1;j<roadLength;j=j+maximumIntervalOfLength){
                Road road = (Road)StandardEntityFactory.makeEntity(StandardEntityURN.ROAD,new EntityID(i));
                road.setX(i + maximumIntervalOfWidth/2.0);
                road.setY(j + maximumIntervalOfLength/2.0);
                worldInfo.addEntity(road);
            }
        }
    }

//    private void createRaver(WorldInfo worldInfo,Config config){
//
//    }
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
            roller.setIndex(i);
            worldInfo.addEntity(roller);
        }
    }

    private void createTrack(WorldInfo worldInfo,Config config){
//        int roadWidth = Integer.valueOf(config.getValue("RoadWidth"));
//        int roadLength = Integer.valueOf(config.getValue("RoadLength"));
        int numOfLTrack = Integer.valueOf(config.getValue("LTrack-Num"));
        for (int i=1;i<=numOfLTrack;i++){
            Track track = new Track(new EntityID(i));
            String trackX = "LTrack"+i+"-X-L";
            String trackY = "LTrack"+i+"-X-R";
            track.setIndex(i);
            track.setLTrack(true);
            track.setX(Double.valueOf(config.getValue(trackX)));
            track.setY(Double.valueOf(config.getValue(trackY)));
            worldInfo.addEntity(track);
        }

        int numOfRTrack = Integer.valueOf(config.getValue("RTrack-Num"));
        for (int i=1;i<=numOfRTrack;i++){
            Track track = new Track(new EntityID(i));
            String trackX = "RTrack"+i+"-X-L";
            String trackY = "RTrack"+i+"-X-R";
            track.setIndex(i);
            track.setLTrack(false);
            track.setX(Double.valueOf(config.getValue(trackX)));
            track.setY(Double.valueOf(config.getValue(trackY)));
            worldInfo.addEntity(track);
        }
    }
}
