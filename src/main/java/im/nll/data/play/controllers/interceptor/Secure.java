package im.nll.data.play.controllers.interceptor;


import com.auth0.jwt.JWTExpiredException;
import im.nll.data.play.controllers.BaseController;
import im.nll.data.play.models.api.v1.Error;
import im.nll.data.play.models.api.v1.ErrorCode;
import im.nll.data.play.utils.JWTUtils;
import org.apache.commons.lang.StringUtils;
import play.Play;
import play.mvc.Before;
import play.mvc.Http;

import java.util.Map;

/**
 * JWT验证
 *
 * @author <a href="mailto:wuzhiqiang@novacloud.com">wuzq</a>
 * @version Revision: 1.0
 * @date 15/4/27 下午5:38
 */
public class Secure extends BaseController {
    @Before(unless = {"v1.Auth.auth"})
    static void checkAccess() {
        if (Play.mode.isProd()) {
            Error error = new Error();
            error.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);
            Http.Header header = request.headers.get("authorization");
            if (header == null || StringUtils.isEmpty(header.value())) {
                error.setCodeWithDefaultMsg(ErrorCode.CLIENT_ACCESS_DENIED);
                unauthorized(error);
            }
            String auth = header.value();
            try {
                final Map<String, Object> claims = JWTUtils.verify(auth);
                String id = String.valueOf(claims.get("id"));
                session.put("id", id);
                request.args.put("requestLogCustomData", "[USER-" + id + "]");
            } catch (Exception e) {
                if (e instanceof JWTExpiredException) {
                    error.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_TOKEN_EXPIRED);
                    unauthorized(error);
                }
                error.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);
                unauthorized(error);
            }
        }
    }
}
