package main.standard.model;

import main.info.WorldInfo;

public abstract class AbstractModule {
    protected WorldInfo worldInfo;
    public AbstractModule(WorldInfo worldInfo){
        this.worldInfo = worldInfo;
    }
}
