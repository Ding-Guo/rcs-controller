package main.registry;

import main.config.Config;
import main.info.WorldInfo;

public abstract class WorldInitializer {
//    private WorldInfo worldInfo;
    public abstract WorldInfo buildWorld(Config config);
}
