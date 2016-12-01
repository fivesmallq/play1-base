package controllers;

import com.alibaba.fastjson.JSON;
import controllers.dto.UserDto;
import models.User;
import models.api.Jsonable;
import models.api.v1.Error;
import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

import static play.mvc.Http.StatusCode.BAD_REQUEST;
import static play.mvc.Http.StatusCode.CREATED;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/11/30 下午4:47
 */
public class UsersTest extends FunctionalTest {
    @Test
    public void get() throws Exception {

    }

    @Test
    public void create() throws Exception {
        UserDto user = new UserDto();
        user.userName = "test-create";
        Http.Response response = POST("/v1/users", "application/json", JSON.toJSONString(user));
        System.out.println(response.out.toString());
        assertStatus(BAD_REQUEST, response);
        Error error = Jsonable.fromJson(response.out.toString(), Error.class);
        assertEquals("entity.password is Required", error.getMessage());

        user.password = "password";
        response = POST("/v1/users", "application/json", JSON.toJSONString(user));
        System.out.println(response.out.toString());
        assertStatus(CREATED, response);
        User ret = Jsonable.fromJson(response.out.toString(), User.class);
        assertEquals(user.userName, ret.userName);
    }

    @Test
    public void list() throws Exception {

    }

}
