package im.nll.data.play.models;

import javax.persistence.Entity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 用户密钥模型
 *
 * @author <a href="mailto:zhaoxiaoyong@novacloud.com">zhaoxiaoyong</a>
 * @version Revision: 1.0
 * @date Aug 5, 2014
 */
@Entity(name = "keypair")
public class KeyPair extends BaseModel {
    String clientId;
    String clientName;
    String secretKey;
    String accessKey;
    boolean enabled = true;
    Date createDate = Date.from(Instant.now());
    Date expireDate = Date.from(Instant.now().plus(365L, ChronoUnit.DAYS));

    /**
     * client Id
     */
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * client name
     *
     * @return
     */
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * secret key
     */

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * access (public) key
     */
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * key enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * key generate date
     */
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * key expire date
     */
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }


    // String remark;//暂时不用
}
