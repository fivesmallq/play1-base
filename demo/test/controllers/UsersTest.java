package controllers;

import com.alibaba.fastjson.JSON;
import models.User;
import models.api.Jsonable;
import models.api.v1.Error;
import models.api.v1.ErrorCode;
import org.junit.Test;
import play.mvc.Http;
import play.test.FunctionalTest;

import static play.mvc.Http.StatusCode.CREATED;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/11/30 下午4:47
 */
public class UsersTest extends FunctionalTest{
    @Test
    public void get() throws Exception {

    }

    @Test
    public void create() throws Exception {
        User user=new User();
        user.userName="test-create";
        user.password="password";
        Http.Response response = POST("/v1/users", "application/json", JSON.toJSONString(user));
        System.out.println(response.out.toString());
        assertStatus(CREATED, response);
        User ret = Jsonable.fromJson(response.out.toString(), User.class);
        assertEquals(user.userName, ret.userName);
    }

    @Test
    public void list() throws Exception {

    }

}
