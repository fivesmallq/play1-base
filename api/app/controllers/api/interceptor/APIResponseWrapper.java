package controllers.api.interceptor;

import controllers.api.RequestId;
import org.apache.commons.lang.StringUtils;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * 设置一些 api response 通用参数. 比如返回的 header 和 cros
 *
 * @author <a href="mailto:wuzhiqiang@novacloud.com">wuzq</a>
 * @version Revision: 1.0
 * @date 15/6/17 下午4:48
 */
public class APIResponseWrapper extends Controller {

    @Before
    static void headers() {
        //set default content type
        response.setContentTypeIfNotSet("application/json; charset=utf-8");
        //request id
        if (StringUtils.isNotEmpty(session.getId())) {
            response.setHeader("X-Session-Id", session.getId());
        }
        if (StringUtils.isNotEmpty(RequestId.getId())) {
            response.setHeader("X-Request-Id", RequestId.getId());
        }
        //set cors
        response.accessControl("*");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Requested-With");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PATCH, PUT, DELETE");
    }
}
