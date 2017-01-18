package controllers.api.response;

import com.alibaba.fastjson.JSON;
import play.exceptions.UnexpectedException;
import play.mvc.Http;
import play.mvc.results.Status;

import java.io.IOException;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/9/3 下午5:01
 */
public class Created extends Status {
    String json;
    public Created(Object data) {
        super(Http.StatusCode.CREATED);
        this.json=JSON.toJSONString(data);
    }

    public Created(String json) {
        super(Http.StatusCode.CREATED);
        this.json = json;
    }

    @Override
    public void apply(Http.Request request, Http.Response response) {
        response.status = Http.StatusCode.CREATED;
        try {
            response.out.write(json.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }
}
