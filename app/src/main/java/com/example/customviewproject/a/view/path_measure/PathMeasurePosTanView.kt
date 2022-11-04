package com.example.customviewproject.a.view.path_measure

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customviewproject.R
import com.example.customviewproject.ext.diagonalDistance
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.getBitMap


/**
 *
 * @ClassName: PathMeasureView
 * @Author: 史大拿
 * @CreateDate: 11/1/22$ 3:15 PM$
 * TODO 参考文档:https://cloud.tencent.com/developer/article/2027721
 */
class PathMeasurePosTanView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private val RADIUS = 100.dp
    }

    private val path by lazy {
        Path().apply {
            addCircle(0f, 0f, RADIUS, Path.Direction.CCW)
//            lineTo(100.dp,100.dp)
        }
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            style = Paint.Style.STROKE
            strokeWidth = 5.dp
        }

    private val pathMeasure by lazy {
        PathMeasure(path, false)
    }

    var current = 0.95f
    private val animator by lazy {
        val animator = ObjectAnimator.ofFloat(1f, 0f)
        animator.duration = 2000
        animator.repeatMode = ObjectAnimator.RESTART
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.interpolator = null
        animator.addUpdateListener {
            val v = it.animatedValue as Float
            current = v
            invalidate()
        }
        animator
    }

    init {
        animator.start()
    }


    private val bitmap = getBitMap(R.mipmap.feiji, 50.dp.toInt())

    private val mMatrix = Matrix()

    private val pos = FloatArray(2)
    private val tan = FloatArray(2)
    override fun onDraw(canvas: Canvas) {
        canvas.translate(width / 2f, height / 2f)

        paint.color = Color.BLACK
        canvas.drawPath(path, paint)

        val distance = pathMeasure.length * current
        /*
         * 作者:史大拿
         * 创建时间: 11/4/22 4:21 PM
         * TODO
         * @param distance: 获取当前path上的点，将值赋值给pos
         * @pos[0]: 获取到x的位置
         * @pos[1]: 获取到y的位置
         * @tan[0]:
         * @tan[1]:
         */
        if (pathMeasure.getPosTan(distance, pos, tan)) {

            val ab = pos[1]
            val ob = pos[0]
            val oa = ab.diagonalDistance(ob)
            Log.i(
                "szjPosTan",
                "pos:${pos.contentToString()}" +
                        "cos:${Math.toRadians(Math.cos((ab / oa).toDouble()))}\t" +
                        "sin:${Math.toRadians(Math.cos((ob / oa).toDouble()))}\t" +
                        "tan:${tan.contentToString()}\t:"
            )
            paint.color = Color.YELLOW

//            Log.i("szjXX", "ab:${ab}\tob:${ob}")

//            canvas.drawLine(150.dp, 150.dp, pos[0], pos[1], paint)

            val dx = pos[0] - bitmap.width / 2f
            val dy = pos[1] - bitmap.height / 2f
//
            mMatrix.reset()

            //
            // 弧度转角度
            val degrees = Math.toDegrees(kotlin.math.atan2(tan[1].toDouble(), tan[0].toDouble()))
            Log.i("szjDegrees", "degrees:${degrees}")
//
            mMatrix.postRotate(degrees.toFloat(), bitmap.width / 2f, bitmap.height / 2f)
            mMatrix.postTranslate(dx, dy)
            canvas.drawBitmap(bitmap, mMatrix, paint)
        }
    }
}