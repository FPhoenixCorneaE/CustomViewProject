package com.example.customviewproject.d.view.d3

import android.graphics.Color

/**
 *
 * @ClassName: UnLockBaseAdapter
 * @Author: 史大拿
 * @CreateDate: 9/13/22$ 2:36 PM$
 * TODO
 */
abstract class UnLockBaseAdapter {
    // 设置宫格个数
    // 例如输入3: 表示3*3
    abstract fun getNumber(): Int

    // 设置样式
    abstract fun getStyle(): JiuGonGeUnLockView.Style

    // 设置原始颜色
    open fun getOriginColor(): Int = let {
        return Color.parseColor("#D8D9D8")
    }

    // 设置按下颜色
    open fun getDownColor(): Int = let {
        return Color.parseColor("#3AD94E")
    }

    // 设置抬起颜色
    open fun getUpColor(): Int = let {
        return Color.parseColor("#57D900")
    }

    // 设置错误颜色
    open fun getErrorColor(): Int = let {
        return Color.parseColor("#D9251E")
    }
}