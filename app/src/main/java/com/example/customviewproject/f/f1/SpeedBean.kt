package com.example.customviewproject.f.f1

/**
 *
 * @ClassName: SpeedBean
 * @Author: 史大拿
 * @CreateDate: 9/23/22$ 8:33 PM$
 * TODO
 */
data class SpeedBean(
    var x: Float, // 其实坐标
    var y: Float,

    var color: Int, // 颜色
    var radius: Float, // 半径

    var vX: Float = 10f, // 速度
    var vY: Float = 10f,

)