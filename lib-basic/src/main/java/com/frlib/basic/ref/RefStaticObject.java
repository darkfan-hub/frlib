package com.frlib.basic.ref;

import java.lang.reflect.Field;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
@SuppressWarnings("unchecked")
public class RefStaticObject<T> {
    private Field field;

    public RefStaticObject(Class<?> cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public Class<?> type() {
        return field.getType();
    }

    public T get() {
        T obj = null;
        try {
            obj = (T) this.field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void set(T obj) {
        try {
            this.field.set(null, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}