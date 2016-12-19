package utils;


import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class BeanMapper {


    public static <S, D> D map(S source, Class<D> destinationClass) {
        D d = Reflect.on(destinationClass).create().get();
        try {
            BeanUtils.copyProperties(d, source);
        } catch (Exception e) {
            throw new RuntimeException("map data error! target class:" + destinationClass);
        }
        return d;
    }


    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> dList = Lists.newArrayList();
        for (S s : sourceList) {
            D d = map(s, destinationClass);
            dList.add(d);
        }
        return dList;
    }

}
