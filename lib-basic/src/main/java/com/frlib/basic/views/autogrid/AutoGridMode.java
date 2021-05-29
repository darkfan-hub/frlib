package com.frlib.basic.views.autogrid;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 10:26
 * @desc 网格显示类型
 */
@IntDef({AutoGridMode.GRID_NINE,
        AutoGridMode.GRID_NORMAL})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoGridMode {
    /**
     * 网格图的两种模式中的九宫格模式
     */
    int GRID_NINE = 0;
    /**
     * 网格图的两种模式中的普通模式
     */
    int GRID_NORMAL = 1;
}
