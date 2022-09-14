package com.example.customviewproject.d.view.d3

/**
 *
 * @ClassName: UnLockBean
 * @Author: 史大拿
 * @CreateDate: 9/13/22$ 9:40 AM$
 * TODO
 */
data class UnLockBean(
    val x: Float, // x坐标
    val y: Float, // y坐标
    val index: Int, // 下标
    /*
     * 作者:史大拿
     * 创建时间: 9/14/22 11:03 AM
     * 当前类型(主要用来区分画笔颜色)
     *  - ORIGIN // 原始 (灰色)
     *  - DOWN // 按下 (浅绿色)
     *  - UP // 抬起 (深绿色)
     *  - ERROR // 错误 (红色)
     */
    var type: JiuGonGeUnLockView.Type =
        JiuGonGeUnLockView.Type.ORIGIN,
)