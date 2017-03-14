package deploy;

import play.Play;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/14 下午3:25
 */
public class Deploy {
    public static Mode mode = Mode.DEV;

    static {
        mode = Mode.valueOf(Play.configuration.getProperty("deploy.mode", "DEV").toUpperCase());
    }

    /**
     * 2 modes
     */
    public enum Mode {

        /**
         * Enable development-specific features, e.g. view the documentation at the URL {@literal "/@documentation"}.
         */
        DEV,

        TEST,
        /**
         * Disable development-specific features.
         */
        PROD;

        public boolean isDev() {
            return this == DEV;
        }

        public boolean isTest() {
            return this == TEST;
        }

        public boolean isProd() {
            return this == PROD;
        }
    }
}
