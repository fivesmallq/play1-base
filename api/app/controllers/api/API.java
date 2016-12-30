package controllers.api;

import com.alibaba.fastjson.JSON;
import controllers.api.interceptor.*;
import play.mvc.With;
import utils.StringUtils;

@With({APIRequestWrapper.class, APIRateLimiter.class, RequestLog.class, ExceptionCatcher.class, Gzip.class, APIResponseWrapper.class})
public class API extends BaseController {

    public static void notFoundPage() {
        if (request.method.equals("OPTIONS")) {
            ok();
        }
        notFound("请求地址错误");
    }

    /**
     * read body data from request. convert to model type and validate it.
     * @param clazz
     * @param <T>
     * @return
     */
    protected static <T> T readBody(Class<T> clazz) {
        String body = request.params.get("body");
        if (StringUtils.isNullOrEmpty(body)) {
            badRequest("request body is required");
        }
        T data= JSON.parseObject(body, clazz);
        validate(data);
        return data;
    }
}
