package com.frlib.basic.ref;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
public class RefGenericSuperclass {

    public static boolean findActualType(Class<?> cls, Class<?> actualType) {
        Type superclass = cls.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) superclass).getActualTypeArguments();
            for (Type type : actualTypes) {
                if (actualType.isAssignableFrom((Class<?>) type)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Class<?> findActualTypeClass(Class<?> cls, Class<?> actualType) {
        Type superclass = cls.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) superclass).getActualTypeArguments();
            Class<?> clazz;
            for (Type type : actualTypes) {
                clazz = (Class<?>) type;
                if (actualType.isAssignableFrom(clazz)) {
                    return clazz;
                }
            }
        }

        return null;
    }
}
