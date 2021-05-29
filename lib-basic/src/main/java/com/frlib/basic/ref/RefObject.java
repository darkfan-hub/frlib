package com.frlib.basic.ref;

import java.lang.reflect.Field;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
@SuppressWarnings("unchecked")
public class RefObject<T> {
    private Field field;

    public RefObject(Class<?> cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public T get(Object object) {
        try {
            return (T) this.field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean set(Object obj, T value) {
        try {
            this.field.set(obj, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}