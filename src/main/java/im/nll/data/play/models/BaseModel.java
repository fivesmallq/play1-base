package models;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/7/25 下午5:50
 */
public class BaseModel extends play.db.jpa.Model implements Jsonable {
    @Id
    @GeneratedValue
    @JSONField
    public Long id;

}
