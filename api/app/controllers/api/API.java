package controllers.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import controllers.api.interceptor.*;
import play.mvc.With;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@With({APIRequestWrapper.class, RequestLog.class, ExceptionCatcher.class, Gzip.class, APIResponseWrapper.class})
public class API extends BaseController {

    public static void notFoundPage() {
        if (request.method.equals("OPTIONS")) {
            ok();
        }
        notFound(request.path + " not found");
    }

    /**
     * read body data from request. convert to model type and validate it.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected static <T> T readBody(Class<T> clazz) {
        String body = request.params.get("body");
        if (StringUtils.isNullOrEmpty(body)) {
            badRequest("request body is required");
        }
        T data = JSON.parseObject(body, clazz);
        validate(data);
        return data;
    }

    /**
     * read body list data from request. convert to model type and validate it.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected static <T> List<T> readBodyList(Class<T> clazz) {
        String body = request.params.get("body");
        if (StringUtils.isNullOrEmpty(body)) {
            badRequest("request body is required");
        }
        List<T> list = JSON.parseObject(body, new TypeReference<List<T>>() {
        });
        List<T> typedList = new ArrayList<>();
        for (T data : list) {
            T newData = JSON.toJavaObject((JSON) data, clazz);
            validate(newData);
            typedList.add(newData);
        }
        return typedList;
    }

    /**
     * set total count header.
     *
     * @param totalCount
     */
    protected static void setTotalCount(Object totalCount) {
        response.setHeader("X-Total-Count", String.valueOf(totalCount));
    }

    /**
     * set total count header to 0 and render empty list.
     */
    protected static void renderEmptyList() {
        response.setHeader("X-Total-Count", "0");
        renderJSON(new ArrayList<>());
    }
}
