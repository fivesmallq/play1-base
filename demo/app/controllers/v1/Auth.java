package controllers.v1;

import controllers.api.API;
import controllers.dto.UserDto;
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
        UserDto dto = readBody(UserDto.class);
        User user = mapDto(dto, User.class);
        //check password
        String jwt = JWTUtils.sign(user.id);
        Token token = new Token(jwt);
        token.userId = String.valueOf(user.id);
        token.userName = user.userName;
        renderJSON(token);
    }
}
