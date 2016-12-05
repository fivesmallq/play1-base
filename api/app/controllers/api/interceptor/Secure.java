package controllers.api.interceptor;


import com.auth0.jwt.JWTExpiredException;
import controllers.api.BaseController;
import models.api.Error;
import models.api.ErrorCode;
import org.apache.commons.lang.StringUtils;
import play.mvc.Before;
import play.mvc.Http;
import utils.JWTUtils;
import utils.Utility;

import java.util.Map;

/**
 * JWT验证
 *
 * @author <a href="mailto:wuzhiqiang@novacloud.com">wuzq</a>
 * @version Revision: 1.0
 * @date 15/4/27 下午5:38
 */
public class Secure extends BaseController {
    @Before
    static void checkAccess() {
        if (Utility.skip(Secure.class, request)) {
            return;
        }
        if (Utility.secure()) {
            Error error = new Error();
            error.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);
            Http.Header header = request.headers.get("authorization");
            if (header == null || StringUtils.isEmpty(header.value())) {
                error.setCodeWithDefaultMsg(ErrorCode.CLIENT_ACCESS_DENIED);
                unauthorized(error);
            }
            String auth = StringUtils.substringAfter(header.value(), "Bearer").trim();
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
