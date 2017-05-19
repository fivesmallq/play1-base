package controllers.v1.interceptor;

import controllers.api.BaseController;
import play.mvc.Before;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/5/19 上午11:40
 */
public class GetClaims extends BaseController {
    @Before(priority = 11)
    static void printClaims() {
        System.out.println("claims:" + claims());
    }
}
