package com.frlib.basic.ref;

import java.lang.reflect.Field;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 反射相关
 */
public class RefInt {
    private Field field;

    public RefInt(Class cls, Field field) throws NoSuchFieldException {
        this.field = cls.getDeclaredField(field.getName());
        this.field.setAccessible(true);
    }

    public int get(Object object) {
        try {
            return this.field.getInt(object);
        } catch (Exception e) {
            return 0;
        }
    }

    public void set(Object obj, int intValue) {
        try {
            this.field.setInt(obj, intValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}