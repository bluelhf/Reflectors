package io.github.bluelhf.reflectors;

import io.github.bluelhf.reflectors.dynamics.InstanceReference;
import io.github.bluelhf.reflectors.statics.ClassReference;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Reflectors {
    private static Logger logger;
    private static ArrayList<LogRecord> log = new ArrayList<>();
    static {
        logger = Logger.getLogger(Reflectors.class.getSimpleName());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.FINEST);
        logger.addHandler(new Handler() {
            @Override public void publish(LogRecord record) { log.add(record); }
            @Override public void flush() { }
            @Override public void close() throws SecurityException { }
        });
    }
    public static ClassReference refer(String classpath) {
        logger.finest("Creating reference to " + classpath + " if possible.");
        return new ClassReference(classpath);
    }
    public static ClassReference refer(Class<?> clazz) {
        logger.finest("Creating reference to " + clazz.getCanonicalName());
        return new ClassReference(clazz);
    }
    public static InstanceReference reflect(Object object) {
        logger.finest("Creating instance reference to " + object.getClass().getCanonicalName());
        return new InstanceReference(object);
    }

    public static void warn(String action, Throwable e) {
        Reflectors.getLogger().warning("Got " + e.getClass().getSimpleName() + " when " + action + ":");
        Reflectors.getLogger().warning(e.getLocalizedMessage());
        Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEachOrdered(s -> Reflectors.getLogger().warning("  " + s));
    }

    public static Logger getLogger() {
        return logger;
    }

    public static ArrayList<LogRecord> getLog() {
        return log;
    }

    public static void tellMeWhatsWrongPlease() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ssX");
        Reflectors.getLog().stream().map(lr -> "[" + lr.getInstant().atOffset(ZoneOffset.UTC).format(formatter) + "] [" + lr.getLevel() + "] " + lr.getMessage()).forEachOrdered(System.out::println);
    }
}
