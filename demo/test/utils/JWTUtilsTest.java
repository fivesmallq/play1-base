package utils;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;
import play.Play;

import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2017/3/22 上午10:23
 */
public class JWTUtilsTest {
    @Test
    public void sign() throws Exception {
        Play.readConfiguration();
        String jwt = JWTUtils.sign("1");
        Map<String, Object> claims = JWTUtils.verify(jwt);
        Assert.assertEquals("1", claims.get("aud"));
        Assert.assertNotNull(claims.get("exp"));
        Assert.assertNotNull(claims.get("iat"));
    }

    @Test
    public void sign1() throws Exception {
        Play.readConfiguration();
        final long iat = System.currentTimeMillis() / 1000l; // issued at claim
        Long exp = iat + 100000;
        String domain = "www.baidu.com";
        String jwt = JWTUtils.sign("1", ImmutableMap.of("domain", "www.baidu.com", "exp", exp));
        Map<String, Object> claims = JWTUtils.verify(jwt);
        Assert.assertEquals(domain, claims.get("domain"));
        Assert.assertEquals("1", claims.get("aud"));
        Assert.assertEquals(exp, TypeUtils.castToLong(claims.get("exp")));
    }

    @Test
    public void verify() throws Exception {

    }

}
