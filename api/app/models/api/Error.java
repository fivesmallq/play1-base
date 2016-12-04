package models.api;


import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用于api服务器端返回客户端的响应
 *
 * @author zhaoxy
 */
public final class Error implements Jsonable {
    private static final long serialVersionUID = 1L;

    /**
     * 返回信息代码（参见{@link ErrorCode} ）
     */
    private Integer code = ErrorCode.SUCCESS;

    /**
     * 返回的消息描述
     */
    private String message = "";

    /**
     * 详细消息描述信息（如：后台详细的异常信息）
     */
    private String detail;

    public Error() {
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setCodeWithDefaultMsg(int code) {
        this.code = code;
        this.message = ErrorCode.getMsg(code);
    }

    public void setCodeMsg(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetailWithExecption(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        this.detail = stringWriter.toString();
    }

    public boolean success() {
        return ErrorCode.SUCCESS == this.code;
    }

    public static Error client(String message) {
        Error error = new Error();
        error.code = ErrorCode.CLIENT_FORMAT_ERROR;
        error.message = message;
        return error;
    }

    public static Error paramMiss(Object... param) {
        Error error = new Error();
        error.code = ErrorCode.CLIENT_RESOURCE_NOT_FOUND;
        error.message = "missing parameter [" + StringUtils.join(param, ",") + "]";
        return error;
    }

    public static Error notFoundBy(Object... resourceIds) {
        Error error = new Error();
        error.code = ErrorCode.CLIENT_RESOURCE_NOT_FOUND;
        error.message = "resource not found,resource ids [" + StringUtils.join(resourceIds, ",") + "]";
        return error;
    }

    public static Error notFound(String message) {
        Error error = new Error();
        error.code = ErrorCode.CLIENT_RESOURCE_NOT_FOUND;
        error.message = message;
        return error;
    }

    public static Error server(String message) {
        Error error = new Error();
        error.code = ErrorCode.SERVER_INTERNAL_ERROR;
        error.message = message;
        return error;
    }
}
