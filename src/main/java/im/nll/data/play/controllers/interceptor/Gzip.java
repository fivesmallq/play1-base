package im.nll.data.play.controllers.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import play.Play;
import play.mvc.Controller;
import play.mvc.Finally;
import utils.Compression;

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
        if (gzipDisabled && response != null) {
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
