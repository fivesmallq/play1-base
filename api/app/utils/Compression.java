package utils;

import play.mvc.Http;

import java.io.*;
import java.util.zip.GZIPOutputStream;


/**
 * @see http://code.google.com/p/htmlcompressor/
 * @see http://developer.yahoo.com/yui/compressor/
 * @see https://github.com/playframework/play/pull/240
 */
public class Compression {


    /**
     * @return Whether the request browser supports GZIP encoding
     */
    public static boolean isGzipSupported(final Http.Request request) {
        // key must be lower-case.
        final Http.Header encodingHeader = request.headers.get("accept-encoding");
        return (encodingHeader != null && encodingHeader.value().contains("gzip"));
    }


    /**
     * creates a gzipped ByteArrayOutputStream which can be used as response.out
     */
    public static ByteArrayOutputStream getGzipStream(final String input) throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        final ByteArrayOutputStream stringOutputStream = new ByteArrayOutputStream(input.length());
        final OutputStream gzipOutputStream = new GZIPOutputStream(stringOutputStream);
        final byte[] buf = new byte[5000];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            gzipOutputStream.write(buf, 0, len);
        }
        inputStream.close();
        gzipOutputStream.close();
        return stringOutputStream;
    }

}
