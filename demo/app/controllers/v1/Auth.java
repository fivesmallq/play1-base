package controllers.v1;

import controllers.api.API;
import controllers.api.interceptor.Secure;
import controllers.dto.UserDto;
import controllers.v1.interceptor.GetClaims;
import models.User;
import models.api.Token;
import play.mvc.With;
import utils.JWTUtils;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/12/1 下午3:09
 */
@With({Secure.class, GetClaims.class})
public class Auth extends API {
    public static void auth() {
        UserDto dto = readBody(UserDto.class);
        User user = mapDto(dto, User.class);
        //check password
        String jwt = JWTUtils.sign(user.id);
        Token token = new Token(jwt);
        token.userId = String.valueOf(user.id);
        token.userName = user.userName;
        setJWTCookie(jwt, "2d");
        renderJSON(token);
    }

    public static void auth2() {
        printTokenCookie();
        //check password
        String jwt = JWTUtils.sign("1");
        Token token = new Token(jwt);
        token.userId = String.valueOf("1");
        token.userName = "userName";
        setJWTSessionCookie(jwt);
        renderJSON(token);
    }

    public static void auth3() {
        printTokenCookie();
        //check password
        String jwt = JWTUtils.sign("1");
        Token token = new Token(jwt);
        token.userId = String.valueOf("1");
        token.userName = "userName";
        setJWTCookie(jwt, "2d");
        renderJSON(token);
    }

    private static void printTokenCookie() {
        System.out.println("claims in action:" + claims());
        System.out.println("session user id:" + session.get("id"));
        if (request.cookies.get("jwt") != null) {
            System.out.println(request.cookies.get("jwt").value);
        } else {
            System.out.println("no jwt cookies");
        }
    }
}
