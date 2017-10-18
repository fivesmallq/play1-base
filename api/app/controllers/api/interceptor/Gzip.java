package controllers.api.interceptor;

import com.google.common.base.Splitter;
import com.google.common.net.MediaType;
import org.apache.commons.lang.StringUtils;
import play.Play;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.Http;
import utils.Compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * response gzip压缩.
 */
public class Gzip extends Controller {
    private static boolean gzipDisabled = true;
    private static String contentTypes = Play.configuration.getProperty("gzip.contentTypes", "text,json,xml");


    static {
        String prop = Play.configuration.getProperty(
                "gzip.api", "false");
        gzipDisabled = Boolean.parseBoolean(prop);
    }

    @Finally
    public static void compress() throws IOException {
        if (gzipDisabled && response != null && isGzipSupported(request)) {
            String contentType = response.contentType;
            if (StringUtils.isNotEmpty(contentType)) {
                // only gzip text,json,xml format
                String subtype = MediaType.parse(contentType).subtype();
                List<String> gzipContypes = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(contentTypes);
                if (!gzipContypes.contains(subtype)) {
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
