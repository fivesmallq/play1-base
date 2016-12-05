package controllers.api.interceptor;


import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import controllers.api.BaseController;
import models.api.Error;
import models.api.ErrorCode;
import org.apache.commons.lang.StringUtils;
import play.Play;
import play.mvc.Before;
import play.mvc.Http;
import utils.Logs;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 实现 API 的限流
 */
public class APIRateLimiter extends BaseController {
    private static Cache<String, RateLimiter> cache = CacheBuilder.newBuilder().initialCapacity(1000)
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    //5 qps
    private static String API_LIMIT = Play.configuration.getProperty("api.limit", "10");

    private static final double DEFAULT_LIMIT = Double.valueOf(API_LIMIT);

    @Before
    static void rateLimiting() {
        try {
            Error error = new Error();
            Http.Header header = request.headers.get("authorization");
            String key;
            if (header != null && StringUtils.isNotEmpty(header.value())) {
                key = StringUtils.substringAfter(header.value(), "Bearer").trim();
            } else {
                String ip = request.remoteAddress;
                if (StringUtils.isNotEmpty(ip) && !ip.startsWith("10.") && !ip.startsWith("192.")) {
                    //只返回第一个ip
                    ip = Splitter.on(",").trimResults().omitEmptyStrings().split(ip).iterator().next();
                } else {
                    Logs.error("remote address is empty!");
                    ip = UUID.randomUUID().toString();
                }
                key = ip;
            }
            RateLimiter rateLimiter = cache.get(key, () -> RateLimiter.create(DEFAULT_LIMIT));
            if (!rateLimiter.tryAcquire()) { //未请求到limiter则返回超额提示
                error.setCodeWithDefaultMsg(ErrorCode.CLIENT_OVER_QUOTA);
                forbidden(error);
            }
        } catch (Exception e) {
            Logs.error("get rate limiter error", e);
        }

    }
}
