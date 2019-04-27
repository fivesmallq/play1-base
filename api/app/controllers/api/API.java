package controllers.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import controllers.api.interceptor.*;
import org.apache.commons.beanutils.BeanUtils;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import play.Play;
import play.libs.Time;
import play.mvc.With;
import utils.StringUtils;
import utils.TypeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@With({APIRequestWrapper.class, RequestLog.class, ExceptionCatcher.class, Gzip.class, APIResponseWrapper.class})
public class API extends BaseController {
    private static String COOKIE_TOKEN_NAME = Play.configuration.getProperty("api.token.cookie", "jwt");
    private static String COOKIE_SECURE = Play.configuration.getProperty("application.session.secure", "false");

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
            if (isPrimitiveOrPrimitiveWrapperOrString(clazz)) {
                typedList.add(TypeUtils.cast(data, clazz));
            } else {
                T newData = JSON.toJavaObject((JSON) data, clazz);
                validate(newData);
                typedList.add(newData);
            }
        }
        return typedList;
    }

    /**
     * read queryString data from request. convert to model type and validate it.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected static <T> T readQueryBody(Class<T> clazz) {
        T data = null;
        try {
            data = clazz.newInstance();
            BeanUtils.populate(data, getUrlParams());
        } catch (Exception e) {
            badRequest("Unsupported parameter type");
        }
        validate(data);
        return data;
    }

    /**
     * url query param to map
     * <p>
     * ?name="xinput"&age=10&languages=["java","c","go"]
     *
     * @return
     */
    private static Map<String, Object> getUrlParams() {
        Map<String, List<String>> uriAttributes = new QueryStringDecoder(request.url).getParameters();

        Map<String, Object> paramMap = Maps.newHashMapWithExpectedSize(uriAttributes.size());

        uriAttributes.forEach((key, attributes) -> {
            attributes.forEach(attrVal -> {
                paramMap.put(key, attrVal);
            });
        });

        return paramMap;
    }

    private static boolean isPrimitiveOrPrimitiveWrapperOrString(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class || type == String.class;
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
     * set jwt token cookie with httpOnly and secure deps on your 'application.session.secure' setting.
     *
     * @param jwt
     * @param duration like 2h, 3d
     */
    protected static void setJWTCookie(String jwt, String duration) {
        response.setCookie(COOKIE_TOKEN_NAME, jwt, null, "/", Time.parseDuration(duration), Boolean.parseBoolean(COOKIE_SECURE), true);
    }

    /**
     * set jwt token cookie.
     *
     * @param jwt
     * @param domain
     * @param path
     * @param duration
     * @param secure
     * @param httpOnly
     */
    protected static void setJWTCookie(String jwt, String domain, String path, String duration, boolean secure, boolean httpOnly) {
        response.setCookie(COOKIE_TOKEN_NAME, jwt, domain, path, Time.parseDuration(duration), secure, httpOnly);
    }

    /**
     * set jwt token session cookie with httpOnly and secure deps on your 'application.session.secure' setting.
     *
     * @param jwt
     */
    protected static void setJWTSessionCookie(String jwt) {
        response.setCookie(COOKIE_TOKEN_NAME, jwt, null, "/", null, Boolean.parseBoolean(COOKIE_SECURE), true);
    }


    /**
     * set total count header to 0 and render empty list.
     */
    protected static void renderEmptyList() {
        response.setHeader("X-Total-Count", "0");
        renderJSON(new ArrayList<>());
    }
}
