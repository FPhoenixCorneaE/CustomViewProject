package com.example.customviewproject.a.view.path_effect

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: DiscretePathEffectView
 * @Author: 史大拿
 * @CreateDate: 8/30/22$ 1:26 PM$
 * TODO
 */
class DiscretePathEffectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        strokeWidth = 4.dp
        style = Paint.Style.STROKE

        // segmentLength 是用来拼接的每个线段的长度，
        // deviation 是偏离量
        pathEffect = DiscretePathEffect(20f, 25f)
    }

    val path by lazy {
        HeadOriginLineView.path
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        canvas.drawPath(path, paint)
    }
}