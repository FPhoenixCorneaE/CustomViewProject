package com.example.customviewproject.a.view.a3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.customviewproject.ext.dp

/**
 *
 * @ClassName: A3TextView
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 4:37 PM$
 * TODO
 */
class A3TextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    val text = """
        程九快育参文然，完下天铁定较治及，动K口用址流。 不段过江得知型越层动传，示事和直你霸委住构。 为组业作往做很型，头海你实京广物的，证霸拒京蠢次。 历革济单支命各被而千或，看增方后思联相火义，事经刷翻常来共京阶。 领置满九车统历因并，农律六交龙根准，史青F影电志支。
      把划此文干动带了决火事南中除，名入作例维在O告准壳U。 加新结开现直争海，性大为成取则所前运，斗1世传候织切。
      规等条所离己受活，照那还定本业真采，记隶对道部抖。 并工总北除选拉，准和许了已，个居复各吧。 验什置王根他空圆常增，根见支界成现叫件，光听求头这医气要。 教没件以与效学油选，进张真角值家压提，质三李开京况却。 除带不角电经七铁，律管次领共再八因，话辰准JW东。 心取复米两化别群说，向图音近包全高基，次易U性积豆更。
    """.trimIndent()

    private val textPaint = TextPaint().apply {
        textSize = 20.dp
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
         * 作者:史大拿
         * 创建时间: 8/6/22 4:41 PM
         * @param1: 需要展示的文字
         * @param2: 画笔
         * @param3: 最大宽度(多久换行)
         * @param align: 对齐样式
         * @param spacingmult: 文字之间的间距 (1f表示不乘)
         * @param spacingadd: 行与行之间的间距
         * @param includepad: 行之间是否需要间距
         */
        val static =
            StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        static.draw(canvas)

    }
}