package com.frlib.basic.ref;

import java.lang.reflect.Field;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
public class RefStaticInt {
    private Field field;

    public RefStaticInt(Class<?> cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public int get() {
        try {
            return this.field.getInt(null);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(int value) {
        try {
            this.field.setInt(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}