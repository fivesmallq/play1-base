package controllers.api.interceptor;

import controllers.api.Utility;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Logs;


/**
 * request 日志
 */
public class RequestLog extends Controller {


    @Before
    static void requestLog() {
        if (Utility.skip(RequestLog.class, request)) {
            return;
        }
        if (Utility.requestLog()) {
            if (request.params._contains("debug")) {
                Logs.info("request:[{}] params:[{}] headers:[{}]", request.method + ":" + request.url, params.allSimple(), request.headers);
            } else {
                Logs.info("request:[{}] params:[{}]", request.method + ":" + request.url, params.allSimple());
            }
        }
    }

}
