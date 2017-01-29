package sidben.pogostick.main;

import java.io.File;
import net.minecraftforge.common.config.Configuration;


public class ModConfig
{

    private static Configuration _config;
    private static boolean       _onDebug;

    public static final String   CATEGORY_DEBUG                       = "debug";
    public static final float    DEFAULT_DISTANCE_LIMITER_FOR_PLAYERS = 2.0F;
    public static final float    MIN_DISTANCE_TO_START_BOUNCING       = 1.1F;
    public static final float    MIN_DISTANCE_TO_KEEP_BOUNCING        = 0.3F;
    public static final float    MIN_MOTION_UP                        = 0.5F;
    public static final float    MAX_MOTION_UP                        = 10F;
    public static final float    MIN_DISTANCE_TO_DAMAGE_POGOSTICK     = 3F;
    public static final int      MAX_DAMAGE_TO_POGOSTICK              = 10;



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
