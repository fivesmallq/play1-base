package im.nll.data.play.controllers.response;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/9/29 下午8:30
 */
public class EvilRequest extends play.mvc.results.Error {
  public EvilRequest(String reason) {
    super(444, reason);
  }
}
