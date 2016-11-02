package controllers.interceptor;

import com.auth0.jwt.JWTExpiredException;
import controllers.BaseController;
import models.api.v1.Error;
import models.api.v1.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import play.Play;
import play.mvc.Before;
import play.mvc.Http;
import utils.JWTUtils;

import java.util.Map;

/**
 * JWT验证
 *
 * @author <a href="mailto:wuzhiqiang@novacloud.com">wuzq</a>
 * @version Revision: 1.0
 * @date 15/4/27 下午5:38
 */
public class Secure extends BaseController {
    @Before(unless = {"v1.Auth.auth", "v1.CheckCode.send",
            "v1.firstp2p.Auth.getUser", "v1.firstp2p.Auth.callback", "v1.firstp2p.Auth.appCallback", "" +
            "v1.Loans.getTerm", "v1.Loans.getWithholding", "v1.Loans.loanApplyStatusNotify"})
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
