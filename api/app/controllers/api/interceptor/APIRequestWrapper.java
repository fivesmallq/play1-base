package controllers.api.interceptor;

import controllers.api.RequestId;
import models.api.ObjectId;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import utils.StringUtils;

/**
 * 设置一些 api request 通用参数. 比如请求的 header
 */
public class APIRequestWrapper extends Controller {

    @Before
    static void headers() {
        String requestId = (String) request.args.get("requestId");
        if (StringUtils.isNotNullOrEmpty(requestId)) {
            requestId = new StringBuffer(20).append(ObjectId.get().toString()).append("-").append(requestId).toString();
        } else {
            requestId = ObjectId.get().toString();
        }
        RequestId.setId(requestId);
        //set default accept type
        request.headers.put("Accept", new Http.Header("Accept", "application/json"));
        request.format = "JSON";
    }
}
