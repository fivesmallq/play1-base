package controllers.api.interceptor;

import org.apache.commons.lang.StringUtils;
import play.Play;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.Http;
import utils.Compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * response gzip压缩.
 */
public class Gzip extends Controller {
    private static boolean gzipDisabled = true;


    static {
        String prop = Play.configuration.getProperty(
                "gzip.api", "false");
        gzipDisabled = Boolean.parseBoolean(prop);
    }

    @Finally
    public static void compress() throws IOException {
        if (gzipDisabled && response != null && isGzipSupported(request)) {
            String contentType = response.contentType;
            if (StringUtils.isNotEmpty(contentType) && contentType.contains("image")) {
                return;
            }
            String content = response.out.toString();
            if ("".equals(content)) {
                return; // fix stange chars on suspended requests
            }
            final ByteArrayOutputStream gzip = Compression.getGzipStream(content);
            response.setHeader("Content-Encoding", "gzip");
            response.setHeader("Content-Length", gzip.size() + "");
            response.out = gzip;
        }
    }

    /**
     * @return Whether the request browser supports GZIP encoding
     */
    public static boolean isGzipSupported(final Http.Request request) {
        // key must be lower-case.
        final Http.Header encodingHeader = request.headers.get("accept-encoding");
        return (encodingHeader != null && encodingHeader.value().contains("gzip"));
    }
}
