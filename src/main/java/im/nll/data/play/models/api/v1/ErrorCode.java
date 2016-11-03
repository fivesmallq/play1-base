package im.nll.data.play.models.api.v1;

import java.lang.reflect.Field;
import java.util.Hashtable;
import im.nll.data.play.models.api.Remark;

/**
 * 返回码定义.
 */
public final class ErrorCode {
  private ErrorCode() {
  }

  /**
   * generic code
   */
  @Remark(remark = "未知错误")
  public static final int UNKNOWN = -1;
  @Remark(remark = "成功")
  public static final int SUCCESS = 0;

	/*
     * 客户端错误.
	 */

  /**
   * 消息格式错误
   */
  @Remark(remark = "消息格式错误")
  public static final int CLIENT_FORMAT_ERROR = 1100;
  /**
   * 身份验证失败
   */
  @Remark(remark = "身份验证失败")
  public static final int CLIENT_AUTH_ERROR = 1200;
  @Remark(remark = "身份令牌过期")
  public static final int CLIENT_AUTH_TOKEN_EXPIRED = 1210;
  /**
   * 操作超时
   */
  @Remark(remark = "操作超时")
  public static final int CLIENT_TIMEOUT = 1300;
  /**
   * 访问被拒绝
   */
  @Remark(remark = "访问被拒绝")
  public static final int CLIENT_ACCESS_DENIED = 1400;
  /**
   * 找不到资源
   */
  @Remark(remark = "找不到资源")
  public static final int CLIENT_RESOURCE_NOT_FOUND = 2100;
  /**
   * 余额不足
   */
  @Remark(remark = "余额不足")
  public static final int CLIENT_CREDIT_LOWER_LIMIT = 2400;
  /**
   * 超过配额
   */
  @Remark(remark = "超过配额")
  public static final int CLIENT_OVER_QUOTA = 2500;

	/*
     * 服务端错误
	 */
  /**
   * 服务器内部错误
   */
  @Remark(remark = "内部错误")
  public static final int SERVER_INTERNAL_ERROR = 5000;

  /**
   * 服务器繁忙
   */
  @Remark(remark = "服务器繁忙")
  public static final int SERVER_BUSY = 5100;
  /**
   * 资源不足
   */
  @Remark(remark = "资源不足")
  public static final int SERVER_RESOURCE_LIMIT = 5200;
  /**
   * 服务更新中
   */
  @Remark(remark = "服务更新中")
  public static final int SERVER_UPDATE = 5300;


  static final Hashtable<Integer, String> codeMsgMap = new Hashtable<>(
      20);

  static {
    Field[] fields = ErrorCode.class.getFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Remark.class)) {
        try {
          codeMsgMap.put(field.getInt(null),
              field.getAnnotation(Remark.class).remark());
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static String getMsg(int responseCode) {
    if (codeMsgMap.containsKey(responseCode)) {
      return codeMsgMap.get(responseCode);
    }
    return "未知错误";

  }

}
