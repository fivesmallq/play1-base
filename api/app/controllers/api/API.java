package controllers.api;

import controllers.api.interceptor.*;
import play.mvc.With;

@With({APIRequestWrapper.class, APIRateLimiter.class, RequestLog.class, ExceptionCatcher.class, Gzip.class, APIResponseWrapper.class})
public class API extends BaseController {
    public static void notFoundPage() {
        if (request.method.equals("OPTIONS")) {
            ok();
        }
        notFound("请求地址错误");
    }
}
