package controllers.api.interceptor;


import com.auth0.jwt.JWTExpiredException;
import controllers.api.BaseController;
import controllers.api.Utility;
import models.api.Error;
import models.api.ErrorCode;
import org.apache.commons.lang.StringUtils;
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
    private static String COOKIE_TOKEN_NAME = Play.configuration.getProperty("api.token.cookie", "jwt");
    private static String enableCookieAuth = Play.configuration.getProperty("api.token.cookie.enable", "false");
    private static String requestClaimsName = Play.configuration.getProperty("api.request.claims.name", "claims");
    private static String enableQueryStringAuth = Play.configuration.getProperty("api.token.queryString.enable", "false");
    private static String tokenQueryStringName = Play.configuration.getProperty("api.token.queryString", "jwt");

    @Before(priority = 10)
    static void checkAccess() {
        if (Utility.skip(Secure.class, request)) {
            return;
        }
        if (Utility.secure()) {
            Error error = new Error();
            error.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);
            Http.Header header = request.headers.get("authorization");
            Http.Cookie cookie = request.cookies.get(COOKIE_TOKEN_NAME);
            String authQuery = request.params.get(tokenQueryStringName);
            String token = "";
            if (header != null && StringUtils.isNotEmpty(header.value())) {
                token = header.value();
                //header value with 'Bearer'
                token = StringUtils.substringAfter(token, "Bearer").trim();
            } else if (StringUtils.isNotBlank(authQuery) && Boolean.parseBoolean(enableQueryStringAuth)) {
                token = authQuery;
            } else if (Boolean.parseBoolean(enableCookieAuth)) {
                if (cookie != null && StringUtils.isNotEmpty(cookie.value)) {
                    token = cookie.value;
                }
            } else {
                error.setCodeWithDefaultMsg(ErrorCode.CLIENT_ACCESS_DENIED);
                unauthorized(error);
            }
            try {
                final Map<String, Object> claims = JWTUtils.verify(token);
                String id = String.valueOf(claims.get("aud"));
                session.put("id", id);
                request.args.put(requestClaimsName, claims);
                request.args.put("requestLogCustomData", "[USER-" + id + "]");
                request.args.put("aud", id);
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
