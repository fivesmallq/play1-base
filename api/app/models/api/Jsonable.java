package models.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * Created by xiaoyong on 24/10/15.
 */
public interface Jsonable extends Serializable {

    default String toJson() {
        return JSON.toJSONString(this, filter);
    }

    default String toPrettyJson() {
        return JSON.toJSONString(this, filter, SerializerFeature.PrettyFormat);
    }

    PropertyFilter filter = (source, name, value) -> {
        //不输出关键字段
        if ("entityId".equals(name) || "persistent".equals(name)) {
            return false;
        }
        return true;
    };

    static <T> T fromJson(String jsonString, Class<T> clazz) {
        return JSON.parseObject(jsonString, clazz, Feature.config(JSON.DEFAULT_PARSER_FEATURE, Feature.UseBigDecimal, true));
    }

    static <T> T fromJson(String jsonString, Class<T> clazz, boolean useBigDecimal) {
        return JSON.parseObject(jsonString, clazz, Feature.config(JSON.DEFAULT_PARSER_FEATURE, Feature.UseBigDecimal, useBigDecimal));
    }

    static String toJson(Object object) {
        return JSON.toJSONString(object, filter);
    }

    static String toPrettyJson(Object object) {
        return JSON.toJSONString(object, filter, SerializerFeature.PrettyFormat);
    }


}
