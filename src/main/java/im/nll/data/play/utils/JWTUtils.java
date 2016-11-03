package im.nll.data.play.utils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import im.nll.data.play.models.BaseModel;
import play.Play;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/7/25 下午4:46
 */
public class JWTUtils {
    public static String secret = Play.configuration.getProperty("application.secret");
    public static String tokenExp = Play.configuration.getProperty("token.exp", "3600");

    public static String sign(BaseModel user) {
        final long iat = System.currentTimeMillis() / 1000l; // issued at claim
        final long exp = iat + Long.valueOf(tokenExp); // expires claim. In this case the token expires in 60 * 60 seconds

        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.id);
        claims.put("exp", exp);
        claims.put("iat", iat);
        final String jwt = signer.sign(claims);
        return jwt;
    }

    public static Map verify(String jwt) throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        final JWTVerifier verifier = new JWTVerifier(secret);
        final Map<String, Object> claims = verifier.verify(jwt);
        return claims;
    }
}
