package main.config;

import java.io.File;

public class ConfigInitializer {
    private static final File DEFAULT_PATH = new File(
            System.getProperty( "user.dir" ),
            "config" + File.separator + "launch.cfg" );
    public static Config getConfig(String... args) {
//        Config commandLine = analysis( args );
        Config config = null;
        try {
            if ( DEFAULT_PATH.exists() ) {
                config = new Config( DEFAULT_PATH );
//                config.merge( commandLine );
                return config;
            }
        } catch ( ConfigException e ) {
            e.printStackTrace();
        }
        return config;
    }

}
