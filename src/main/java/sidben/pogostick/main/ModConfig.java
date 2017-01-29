package sidben.pogostick.main;

import java.io.File;
import net.minecraftforge.common.config.Configuration;


public class ModConfig
{

    public static final String  CATEGORY_DEBUG = "debug";

    public static Configuration _config;
    public static boolean       _onDebug;



    public static void init(File configFile)
    {
        if (_config == null) {
            _config = new Configuration(configFile);
            refreshConfig();
        }

    }


    public static void refreshConfig()
    {
        // Load properties
        _onDebug = _config.getBoolean("on_debug", CATEGORY_DEBUG, false, "");

        // saving the configuration to its file
        if (_config.hasChanged()) {
            _config.save();
        }
    }



    // --------------------------------------------
    // Public config values
    // --------------------------------------------

    /**
     * When the mod is on 'debug mode', messages with the level Trace and Debug will be added to the logs.
     */
    public static boolean onDebug()
    {
        return _onDebug;
    }

}
