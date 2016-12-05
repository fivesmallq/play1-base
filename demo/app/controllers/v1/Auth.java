package controllers.v1;

import controllers.api.API;
import models.User;
import models.api.Token;
import utils.JWTUtils;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/12/1 下午3:09
 */
public class Auth extends API {
    public static void auth() {
        User user = readBody(User.class);
        //check password
        String jwt = JWTUtils.sign(user.id);
        Token token = new Token(jwt);
        token.userId = String.valueOf(user.id);
        token.userName = user.userName;
        renderJSON(token);
    }
}
