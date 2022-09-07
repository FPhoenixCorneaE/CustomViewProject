package com.example.customviewproject.d.view.d1

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.example.customviewproject.ext.dp
import com.example.customviewproject.ext.screenWidth

/**
 *
 * @ClassName: KuGouDrawerLayout
 * @Author: 史大拿
 * @CreateDate: 9/7/22$ 7:57 PM$
 * TODO
 */
class KuGouDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    var drawerWidth = 0

    // 当xml解析完成回调
    override fun onFinishInflate() {
        super.onFinishInflate()
        // 获取到的是LinearLayout
        val viewGroup = getChildAt(0) as ViewGroup

        // TODO 抽屉布局
        val drawerView = viewGroup.getChildAt(0)
        // 设置抽屉布局宽度
        val drawerParams = drawerView.layoutParams
        drawerWidth = (screenWidth * 0.7).toInt()
        drawerParams.width = drawerWidth
        drawerView.layoutParams = drawerParams


        // TODO 内容布局
        val contentView = viewGroup.getChildAt(1)
        // 设置抽屉布局宽度
        val contentParams = contentView.layoutParams
        contentParams.width = screenWidth
        contentView.layoutParams = contentParams
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        scrollTo(drawerWidth, 0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.e("szj改变了", "l:${l}\toldL:${oldl} t:${t} oldTop:${oldt}")
    }

}