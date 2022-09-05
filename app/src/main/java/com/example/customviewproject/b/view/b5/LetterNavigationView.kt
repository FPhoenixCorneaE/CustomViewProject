package com.example.customviewproject.b.view.b5

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customviewproject.ext.dp
import kotlin.math.max
import kotlin.math.min

/**
 *
 * @ClassName: LetterNavigationView
 * @Author: 史大拿
 * @CreateDate: 9/5/22$ 4:04 PM$
 * TODO
 */
class LetterNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
    }

    private val letters: ArrayList<String> by lazy {
        val temp = arrayListOf<String>()
        ('A'..'Z').forEach {
            temp.add(it.toString())
        }
        temp.add("#")
        temp
    }

    // 计算每一个字母的高度
    // tips: 这里加paint.fontMetrics.top 是为了不让#太靠下，，
    private val letterHeight by lazy {
        (height + paint.fontMetrics.top) / letters.size
    }

    // @param : -1结束了
    private var moveBlock: ((Int, String) -> Unit)? = null

    fun setCallMoveBlock(block: (Int, String) -> Unit) {
        this.moveBlock = block
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val textWidth = paint.measureText("A")
        val width =
            resolveSize((textWidth + paddingLeft + paddingRight).toInt(), widthMeasureSpec)
        val height =
            resolveSize(measuredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        // 计算基线
        var tempY = -paint.fontMetrics.top + letterHeight / 2f
        letters.forEach {

            // 计算字母的宽度
            // 作用: 为了让字母居中
            val textWidth = paint.measureText(it)
            val tempX = (width) / 2 - textWidth / 2f

            canvas.drawText(
                it,
                0, it.length,
                tempX,
                tempY,
                paint
            )
            tempY += letterHeight
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {

                var moveValue = ((event.y + paint.fontMetrics.top) / (letterHeight)).toInt()

                moveValue = max(moveValue, 0)
                moveValue = min(moveValue, letters.size)

                moveBlock?.invoke(moveValue, letters[moveValue])
                Log.e("szjMove", "$moveValue")
            }

            MotionEvent.ACTION_UP -> {
                postDelayed({
                    moveBlock?.invoke(-1, "")
                }, 2000)
            }
        }
        return true
    }


}