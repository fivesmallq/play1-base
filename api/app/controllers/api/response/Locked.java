package controllers.api.response;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/9/29 下午8:30
 */
public class Locked extends play.mvc.results.Error {
    public Locked(String reason) {
        super(423, reason);
    }
}
