package im.nll.data.play.controllers.response;

import play.mvc.Http;
import play.mvc.results.Status;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/9/3 下午5:01
 */
public class Created extends Status {
  public Created() {
    super(Http.StatusCode.CREATED);
  }
}
