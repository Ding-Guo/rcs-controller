package main.log;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayDeque;
import java.util.Deque;
import org.apache.log4j.LogManager;
import org.apache.log4j.NDC;

public final class Logger {
    private static final InheritableThreadLocal<Deque<org.apache.log4j.Logger>> LOG = new InheritableThreadLocal<Deque<org.apache.log4j.Logger>>() {
        protected Deque<org.apache.log4j.Logger> initialValue() {
            return new ArrayDeque();
        }

        protected Deque<org.apache.log4j.Logger> childValue(Deque<org.apache.log4j.Logger> parent) {
            return new ArrayDeque(parent);
        }
    };

    private Logger() {
    }

    public static void setLogContext(String context) {
        Deque<org.apache.log4j.Logger> queue = (Deque)LOG.get();
        queue.clear();
        queue.addLast(LogManager.getLogger(context));
    }

    public static void pushLogContext(String context) {
        Deque<org.apache.log4j.Logger> queue = (Deque)LOG.get();
        queue.addLast(LogManager.getLogger(context));
    }

    public static void popLogContext() {
        Deque<org.apache.log4j.Logger> queue = (Deque)LOG.get();
        queue.removeLast();
    }

    private static org.apache.log4j.Logger get() {
        Deque<org.apache.log4j.Logger> queue = (Deque)LOG.get();
        return queue.isEmpty() ? LogManager.getRootLogger() : (org.apache.log4j.Logger)queue.getLast();
    }

    public static void pushNDC(String s) {
        NDC.push(s);
    }

    public static void popNDC() {
        NDC.pop();
    }

    public static void trace(String msg) {
        get().trace(msg);
    }

    public static void trace(String msg, Throwable t) {
        get().trace(msg, t);
    }

    public static void debug(String msg) {
        get().debug(msg);
    }

    public static void debug(String msg, Throwable t) {
        get().debug(msg, t);
    }

    public static void info(String msg) {
        get().info(msg);
    }

    public static void info(String msg, Throwable t) {
        get().info(msg, t);
    }

    public static void warn(String msg) {
        get().warn(msg);
    }

    public static void warn(String msg, Throwable t) {
        get().warn(msg, t);
    }

    public static void error(String msg) {
        get().error(msg);
    }

    public static void error(String msg, Throwable t) {
        get().error(msg, t);
    }

    public static void fatal(String msg) {
        get().fatal(msg);
    }

    public static void fatal(String msg, Throwable t) {
        get().fatal(msg, t);
    }
}
