package im.nll.data.play.controllers.interceptor;

import base.RequestId;
import models.ObjectId;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;

/**
 * 设置一些 api request 通用参数. 比如请求的 header
 */
public class APIRequestWrapper extends Controller {

    @Before
    static void headers() {
        String requestId = (String) request.args.get("requestId");
        requestId = new StringBuffer(20).append(ObjectId.get().toString()).append("-").append(requestId).toString();
        RequestId.setId(requestId);
        //set default accept type
        request.headers.put("Accept", new Http.Header("Accept", "application/json"));
        request.format = "JSON";
    }
}
