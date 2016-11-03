package im.nll.data.play.utils;

/**
 * Returns the logger for the calling class. Can be used to determine the
 * Logger, reduces errors when copy&paste.
 * Created by xiaoyong on 18/11/15.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.nll.data.play.controllers.RequestId;
import play.mvc.Scope;


public class Logs {

    private final static int THREAD_TYPE_DEEP = 2;

    /**
     * A custom security manager that exposes the getClassContext() information
     */
    static class CallerSecurityManager extends SecurityManager {

        public String getCallerClassName(int callStackDepth) {
            return getClassContext()[callStackDepth].getName();
        }
    }

    /**
     * Using a Custom SecurityManager to get the caller classname.
     */
    private static final CallerSecurityManager CSM = new CallerSecurityManager();

    /**
     * Get a logger with current class name
     *
     * @return logger
     */
    public static Logger get() {
        String className = CSM.getCallerClassName(THREAD_TYPE_DEEP);
        return determineLogger(className);
    }

    public static void debug(String s, Object... objects) {
        String className = CSM.getCallerClassName(THREAD_TYPE_DEEP);
        determineLogger(className).debug(injectLog(s), objects);
    }

    public static void info(String s, Object... objects) {
        String className = CSM.getCallerClassName(THREAD_TYPE_DEEP);
        determineLogger(className).info(injectLog(s), objects);
    }

    public static void warn(String s, Object... objects) {
        String className = CSM.getCallerClassName(THREAD_TYPE_DEEP);
        determineLogger(className).warn(injectLog(s), objects);
    }

    public static void error(String s, Object... objects) {
        String className = CSM.getCallerClassName(THREAD_TYPE_DEEP);
        determineLogger(className).error(injectLog(s), objects);
    }

    public static String injectLog(String s) {
        Scope.Session session = Scope.Session.current();
        String sessionId = "";
        if (session != null) {
            sessionId = Scope.Session.current().getId();
        }
        String requestId = RequestId.getId();
      if (session != null && StringUtils.isNotNullOrEmpty(sessionId) && StringUtils
          .isNotNullOrEmpty(requestId)) {

            StringBuffer buffer = new StringBuffer(20).append("request-id:").append(requestId).append(" session:").append(sessionId).append(" ").append(s);
            return buffer.toString();
        } else {
            return s;
        }
    }

    /**
     * Determines the class and the appropiate logger of the calling class.
     *
     * @return The (slf4j) logger of the caller
     */
    static Logger determineLogger(String callerClassName) {
        return LoggerFactory.getLogger(callerClassName);
    }

}
