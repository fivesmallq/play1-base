package controllers.api;

import controllers.api.response.Created;
import controllers.api.response.EvilRequest;
import controllers.api.response.NoContent;
import controllers.api.response.Unauthorized;
import models.api.Jsonable;
import models.api.v1.Error;
import models.api.v1.ErrorCode;
import org.apache.commons.lang.StringUtils;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.results.BadRequest;
import play.mvc.results.Forbidden;
import play.mvc.results.NotFound;
import utils.Logs;
import utils.Utility;

/**
 * @author <a href="mailto:yongxiaozhao@gmail.com">zhaoxiaoyong</a>
 * @version Revision: 1.0
 *          date 2016/5/12 9:21
 */
public class BaseController extends Controller {
    /**
     * 验证path参数
     */
    @Before
    protected static void validatePath() {
        request.format = "json";
        if (validation.hasErrors()) {
            for (play.data.validation.Error error : validation.errors()) {
                badRequest(Error.client(error.message()));
            }
        }
    }

    /**
     * 验证传输的对象
     *
     * @param entity
     */
    protected static void validate(Object entity) {
        request.format = "json";
        validation.valid(entity);
        if (validation.hasErrors()) {
            for (play.data.validation.Error error : validation.errors()) {
                badRequest(Error.client(error.message()));
            }
        }
    }

    /**
     * Render object with json format.
     */
    protected static void renderJSON(Object data) {
        renderJSON(Jsonable.toPrettyJson(data));
    }

    /**
     * Send a 201 Created
     */
    protected static void created(Object data) {
        request.format = "json";
        throw new Created(data);
    }

    /**
     * Send a 204 NO CONTENT
     */
    protected static void noContent() {
        request.format = "json";
        throw new NoContent();
    }

    protected static void badRequestIfNull(Object object, String message) {
        if (object == null) {
            request.format = "json";
            Error error = Error.notFound(message);
            badRequest(error);
        }
    }

    /**
     * 恶意请求，返回444
     *
     * @param message
     */
    protected static void evilRequest(String message) {
        request.format = "json";
        Error error = Error.client(message);
        throw new EvilRequest(error.toPrettyJson());
    }

    protected static void badRequest(String message) {
        request.format = "json";
        Error error = Error.client(message);
        badRequest(error);
    }

    protected static void badRequest(Error error) {
        request.format = "json";
        throw new BadRequest(error.toPrettyJson());
    }

    protected static void notFound(String message) {
        request.format = "json";
        throw new NotFound(Error.notFound(message).toPrettyJson());
    }

    protected static void notFoundBy(Object... ids) {
        request.format = "json";
        throw new NotFound(Error.notFoundBy(ids).toPrettyJson());
    }

    protected static void forbidden(Error error) {
        request.format = "json";
        throw new Forbidden(error.toPrettyJson());
    }

    protected static void unauthorized(Error error) {
        request.format = "json";
        throw new Unauthorized(error.toPrettyJson());
    }

    protected static void unauthorized() {
        Error error = new Error();
        error.setCodeWithDefaultMsg(ErrorCode.CLIENT_ACCESS_DENIED);
        request.format = "json";
        throw new Unauthorized(error.toPrettyJson());
    }


    /**
     * 判断用户是否有权限操作当前资源
     *
     * @param userId
     */
    protected static void forbidden(Object userId) {
        if (Utility.forbidden()) {
            String currentId = session.get("id");
            if (utils.StringUtils.isNullOrEmpty(currentId)) {
                Logs.error("current id is empty. @Secure must be annotationed at class: [{}]", request.controller);
            }
            if (StringUtils.isNotEmpty(currentId) && !currentId.equals(String.valueOf(userId))) {
                Error error = new Error();
                error.setCodeWithDefaultMsg(ErrorCode.CLIENT_ACCESS_DENIED);
                request.format = "json";
                throw new Forbidden(error.toPrettyJson());
            }
        }
    }

    /**
     * Send a 500 In request
     */
    protected static void error() {
        request.format = "json";
        Error error1 = new Error();
        error1.setCodeWithDefaultMsg(ErrorCode.SERVER_INTERNAL_ERROR);
        error(error1);
    }


    protected static void error(String message) {
        error(Error.server(message));
    }

    protected static void error(Error error) {
        request.format = "json";
        throw new play.mvc.results.Error(error.toPrettyJson());
    }


}
