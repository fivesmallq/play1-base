package models.api.v1;


import models.api.Jsonable;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/7/24 下午7:04
 */
public class Token implements Jsonable {
    public String token;
    public Long userId;
    public String userName;

    public Token(String token) {
        this.token = token;
    }
}
