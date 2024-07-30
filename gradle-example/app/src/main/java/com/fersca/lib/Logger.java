package com.fersca.lib;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author fersc
 */
public class Logger {

    //constructor privado para evitar el publico por default en una clase static que se usa como utils
    private Logger(){}
    
    //Logger
    private static final java.util.logging.Logger loggerObject;
    
    static {
        loggerObject = java.util.logging.Logger.getLogger("* - ");

        loggerObject.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String FORMAT_STRING = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(FORMAT_STRING,
                        new Date(lr.getMillis()),           //Imprime la fecha
                        lr.getLevel().getLocalizedName(),   //El nombre del level de log
                        lr.getMessage()                     //El mensage
                );
            }
        });
        loggerObject.addHandler(handler);
    }    
    
    public static void println(Object message){        
        String m = message.toString();
        loggerObject.log(Level.INFO, m);        
    }    
    public static void println(Level level, Object message){        
        String m = message.toString();
        loggerObject.log(level, m);        
    }    
    public static void setLogLevel(Level newLvl) {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        rootLogger.setLevel(newLvl);
        for (Handler h : handlers) {
            h.setLevel(newLvl);
        }
    }    
    
}
