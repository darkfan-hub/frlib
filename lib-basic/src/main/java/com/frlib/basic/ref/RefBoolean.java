package com.frlib.basic.ref;

import java.lang.reflect.Field;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
public class RefBoolean {

    private Field field;

    public RefBoolean(Class<?> cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public boolean get(Object object) {
        try {
            return this.field.getBoolean(object);
        } catch (Exception e) {
            return false;
        }
    }

    public void set(Object obj, boolean value) {
        try {
            this.field.setBoolean(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}