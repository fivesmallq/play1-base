package im.nll.data.play.controllers.interceptor;

import com.alibaba.fastjson.JSONException;
import im.nll.data.play.controllers.BaseController;
import im.nll.data.play.models.api.v1.Error;
import im.nll.data.play.models.api.v1.ErrorCode;
import im.nll.data.play.utils.Logs;
import play.Play;
import play.mvc.Catch;

/**
 * @author <a href="mailto:yongxiaozhao@gmail.com">zhaoxiaoyong</a>
 * @version Revision: 1.0
 *          date 2016/5/12 9:52
 */
public class ExceptionCatcher extends BaseController {

    @Catch(value = Throwable.class)
    static void catchThrowable(Throwable throwable) {
        if (throwable instanceof JSONException) {
            if(Play.mode.isDev()) {
                Logs.error("parse json error!", throwable);
            }
            badRequest("input json format error");
        }
        Logs.debug("request:[{}] params:[{}] headers:[{}]", request.method + ":" + request.url, params.allSimple(), request.headers, throwable);
        Logs.error("request:[{}]", request.method + ":" + request.url, throwable);

        Error error = new Error();
        error.setCode(ErrorCode.SERVER_INTERNAL_ERROR);
        error.setMessage("Server throwed Exception:" + throwable.getMessage());
        if (Play.mode.isDev())
            error.setDetailWithExecption(throwable);
        request.format = "json";
        throw new play.mvc.results.Error(error.toPrettyJson());
    }
}
