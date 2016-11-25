package models;

import com.alibaba.fastjson.annotation.JSONField;
import models.api.Jsonable;

import java.util.Date;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/11/24 下午11:46
 */
public class User implements Jsonable {
    public Long id;
    public String userName;
    @JSONField(serialize = false)
    public String password;
    public Date createDate;
    public Integer state;
}
