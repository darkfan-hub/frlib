package com.frlib.basic.ref;

import java.lang.reflect.Field;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
public class RefLong {
    private Field field;

    public RefLong(Class cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public long get(Object object) {
        try {
            return this.field.getLong(object);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(Object obj, long value) {
        try {
            this.field.setLong(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}