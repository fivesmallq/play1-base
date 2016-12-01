package utils;

import play.Play;
import play.mvc.Http;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/12/1 下午2:21
 */
public class Utility {
    private static PropertiesParser prop = new PropertiesParser(Play.configuration);
    private static String[] EMPTY_ARRAY = new String[0];

    public static boolean skip(Class interceptor, Http.Request request) {
        boolean skip = true;
        String[] unless = prop.getStringArrayProperty(interceptor.getSimpleName() + ".unless", EMPTY_ARRAY);
        String[] only = prop.getStringArrayProperty(interceptor.getSimpleName() + ".only", EMPTY_ARRAY);
        for (String un : only) {
            if (!un.contains(".")) {
                un = interceptor.getName().substring(12) + "." + un;
            }
            if (un.equals(request.action)) {
                skip = false;
                break;
            } else {
                skip = true;
            }
        }
        for (String un : unless) {
            if (!un.contains(".")) {
                un = interceptor.getName().substring(12) + "." + un;
            }
            if (un.equals(request.action)) {
                skip = true;
                break;
            }
        }
        return skip;
    }

}
