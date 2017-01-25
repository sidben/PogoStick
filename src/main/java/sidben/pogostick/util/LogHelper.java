package sidben.pogostick.util;


import org.apache.logging.log4j.Level;
import net.minecraftforge.fml.common.FMLLog;
import sidben.pogostick.handler.ConfigurationHandler;
import sidben.pogostick.reference.Reference;


// Based on Pahimar LohHelper class
public class LogHelper
{

    public static void log(Level logLevel, Object object)
    {
        FMLLog.log(Reference.ModName, logLevel, String.valueOf(object));
    }

    public static void all(Object object)
    {
        log(Level.ALL, object);
    }

    public static void debug(Object object)
    {
        log(Level.DEBUG, object);
    }

    public static void error(Object object)
    {
        log(Level.ERROR, object);
    }

    public static void fatal(Object object)
    {
        log(Level.FATAL, object);
    }

    public static void info(Object object)
    {
        if (ConfigurationHandler.onDebug) {
            log(Level.INFO, object);
        }
    }

    public static void off(Object object)
    {
        log(Level.OFF, object);
    }

    public static void trace(Object object)
    {
        log(Level.TRACE, object);
    }

    public static void warn(Object object)
    {
        log(Level.WARN, object);
    }

}