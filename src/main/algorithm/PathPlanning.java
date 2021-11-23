package main.algorithm;

import main.info.WorldInfo;
import main.standard.entities.Road;
import main.standard.messages.Output;
import main.standard.model.AbstractModule;
import main.standard.model.Action;

import java.util.List;

public abstract class PathPlanning extends AbstractModule {
    public PathPlanning(WorldInfo worldInfo) {
        super(worldInfo);
    }

    public abstract PathPlanning calc();
//    public abstract Action getResult();
    public abstract Output getOutput();
}
