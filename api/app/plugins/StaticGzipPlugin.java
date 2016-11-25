package plugins;

import com.google.common.io.Files;
import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.libs.MimeTypes;
import play.mvc.Http;
import play.utils.Utils;
import play.vfs.VirtualFile;
import utils.Logs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static utils.Compression.getGzipStream;
import static utils.Compression.isGzipSupported;

public class StaticGzipPlugin extends PlayPlugin {
    private static boolean gzipDisabled = true;


    static {
        String prop = Play.configuration.getProperty(
                "gzip.static", "false");
        gzipDisabled = Boolean.parseBoolean(prop);
        Logs.info("gizp static config:{}", gzipDisabled);
    }

    @Override
    public boolean serveStatic(VirtualFile file, Http.Request request, Http.Response response) {
        try {
            final File localFile = file.getRealFile();

            String contentType = MimeTypes.getContentType(localFile.getName(), "text/plain");
            // ignore images, I was having trouble when gzipping them. They probably don't need it anyway.
            if (contentType.contains("image")) return false;
            response.setContentTypeIfNotSet(contentType);
            response = addEtag(request, response, localFile);

            String content = Files.toString(localFile, Charset.defaultCharset());

            // gzip only if supported and not excluded
            if (gzipDisabled && isGzipSupported(request)) {
                final ByteArrayOutputStream gzip = getGzipStream(content);
                // set response header
                response.setHeader("Content-Encoding", "gzip");
                response.setHeader("Content-Length", gzip.size() + "");
                response.out = gzip;
                return true;
            } else {
                response.out = new ByteArrayOutputStream(content.length());
                response.out.write(content.getBytes());
                return true;
            }
        } catch (Exception e) {
            Logger.error(e, "Error when Gzipping response: %s", e.getMessage());

        }
        return false;
    }

    private static Http.Response addEtag(Http.Request request, Http.Response response, File file) {
        if (Play.mode == Play.Mode.DEV) {
            response.setHeader(CACHE_CONTROL, "no-cache");
        } else {
            String maxAge = Play.configuration.getProperty("http.cacheControl", "3600");
            if (maxAge.equals("0")) {
                response.setHeader(CACHE_CONTROL, "no-cache");
            } else {
                response.setHeader(CACHE_CONTROL, "max-age=" + maxAge);
            }
        }
        boolean useEtag = Play.configuration.getProperty("http.useETag", "true").equals("true");
        long last = (file.lastModified() / 1000) * 1000;
        final String etag = "\"" + last + "-" + file.hashCode() + "\"";
        if (!request.isModified(etag, last)) {
            if (request.method.equals("GET")) {
                response.status = Http.StatusCode.NOT_MODIFIED;
            }
            if (useEtag) {
                response.setHeader(ETAG, etag);
            }
        } else {
            response.setHeader(LAST_MODIFIED, Utils.getHttpDateFormatter().format(new Date(last)));
            if (useEtag) {
                response.setHeader(ETAG, etag);
            }
        }
        return response;
    }

}
