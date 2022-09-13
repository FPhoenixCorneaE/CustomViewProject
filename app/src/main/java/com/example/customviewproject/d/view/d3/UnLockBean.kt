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
    var type: JiuGonGeUnLockView.Type = JiuGonGeUnLockView.Type.ORIGIN, // 当前类型
)