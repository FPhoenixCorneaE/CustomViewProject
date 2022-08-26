package com.example.customviewproject.c.view.c3

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 *
 * @ClassName: C3Bean
 * @Author: 史大拿
 * @CreateDate: 8/26/22$ 10:27 AM$
 * TODO
 */
data class C3Bean(
    @FloatRange(from = 0.0, to = 1.0) var parallaxTransformInX: Float = 0.0f,  // 入场时候X的坐标
    @FloatRange(from = 0.0, to = 1.0) var parallaxTransformInY: Float = 0.0f,  // 入场时候Y的坐标

    @FloatRange(from = 0.0, to = 1.0) var parallaxTransformOutX: Float = 0.0f, // 出场时候X的坐标
    @FloatRange(from = 0.0, to = 1.0) var parallaxTransformOutY: Float = 0.0f, // 出场时候Y的坐标

    @IntRange(from = 0) var parallaxRotateIn: Int = 0, // 入场旋转 (圈数)
    @IntRange(from = 0) var parallaxRotateOut: Int = 0, // 出场旋转 (圈数)

    @FloatRange(from = 0.0) var parallaxZoomIn: Float = 0f, // 缩放倍数 (入场)
    @FloatRange(from = 0.0) var parallaxZoomOut: Float = 0f, // 缩放倍数 (出场)
) {
    fun isNotEmpty() = let {
        parallaxTransformInX != 0f
                || parallaxTransformInY != 0f

                || parallaxTransformOutX != 0f
                || parallaxTransformOutY != 0f

                || parallaxRotateIn != 0
                || parallaxRotateOut != 0

                || parallaxZoomIn != 0f
                || parallaxZoomOut != 0f
    }
}