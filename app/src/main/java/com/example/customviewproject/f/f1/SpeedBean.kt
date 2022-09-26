package com.example.customviewproject.f.f1

import androidx.annotation.FloatRange

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
    val randomV: Int = ((1 until 5).random()), // 随机速度
    var angle : Float = 0f, // 相对于按下时候的角度

    @FloatRange(from = 0.0, to = 1.0) var collisionWear: Float = 0.9f // 碰撞磨损

)