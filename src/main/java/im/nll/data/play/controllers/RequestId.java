package base;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/9/3 下午4:27
 */
public class RequestId {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String getId() {
        return threadLocal.get();
    }

    public static void setId(String id) {
        threadLocal.set(id);
    }
}
