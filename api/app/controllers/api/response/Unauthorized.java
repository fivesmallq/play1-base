package controllers.api.response;

import play.mvc.Http;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/9/3 下午5:33
 */
public class Unauthorized extends play.mvc.results.Error {
    public Unauthorized(String reason) {
        super(Http.StatusCode.UNAUTHORIZED, reason);
    }
}
