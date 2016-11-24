package controllers.api.interceptor;

import play.mvc.Before;
import play.mvc.Controller;
import utils.Logs;

/**
 * request 日志
 */
public class RequestLog extends Controller {

    @Before(unless = {"v1.Auth.auth", "v1.user.Users.save", "v1.user.UserBanks.save"})
    static void requestLog() {
        if (request.params._contains("debug")) {
            Logs.info("request:[{}] params:[{}] headers:[{}]", request.method + ":" + request.url, params.allSimple(), request.headers);
        } else {
            Logs.info("request:[{}] params:[{}]", request.method + ":" + request.url, params.allSimple());
        }
    }

}
