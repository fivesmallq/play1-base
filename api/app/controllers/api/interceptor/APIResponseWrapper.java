package controllers.api.interceptor;

import controllers.api.RequestId;
import org.apache.commons.lang.StringUtils;
import play.mvc.Controller;
import play.mvc.Finally;

/**
 * 设置一些 api response 通用参数. 比如返回的 header 和 cros
 *
 * @author <a href="mailto:wuzhiqiang@novacloud.com">wuzq</a>
 * @version Revision: 1.0
 * @date 15/6/17 下午4:48
 */
public class APIResponseWrapper extends Controller {

    @Finally
    static void headers() {
        String defaultContentType = "application/json; charset=utf-8";
        //set default content type
        response.setContentTypeIfNotSet(defaultContentType);
        //playframework default content type without utf-8
        if (response.contentType.equals("application/json")) {
            response.contentType = defaultContentType;
        }
        //request id
        if (StringUtils.isNotEmpty(session.getId())) {
            response.setHeader("X-Session-Id", session.getId());
        }
        if (StringUtils.isNotEmpty(RequestId.getId())) {
            response.setHeader("X-Request-Id", RequestId.getId());
        }
        //set cors
        response.accessControl("*");
        if (request.method.equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count");
            response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PATCH, PUT, DELETE");
            response.setHeader("Access-Control-Max-Age", "86400");
        } else {
            response.setHeader("Access-Control-Expose-Headers",
                    "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count");
        }
    }
}
