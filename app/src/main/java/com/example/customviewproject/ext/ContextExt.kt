package com.example.customviewproject.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.example.customviewproject.R
import kotlin.random.Random

/**
 *
 * @ClassName: ContextExt
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 5:42 PM$
 * TODO
 */


// 屏幕的宽高
val screenWidth
    get() = Resources.getSystem().displayMetrics.widthPixels
val screenHeight
    get() = Resources.getSystem().displayMetrics.heightPixels

/*
 * TODO 获取样式
 */
fun Context.getTheme(@AttrRes attrId: Int): Int? = let {
    val typedValue = TypedValue()
    // 解析属性,解析成功范围true
    if (theme.resolveAttribute(attrId, typedValue, true)) {
        return typedValue.data
    }
    return null
}

/*
 * 作者:android 超级兵
 * 创建时间: 4/13/22 2:26 PM
 * TODO 随机颜色
 */
@SuppressLint("ResourceType")
fun Context.randomColor(@IntRange(from = 0, to = 255) alpha: Int = 255): Int = let {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val random = Random
        return@let Color.argb(
            alpha,
            random.nextInt(225),
            random.nextInt(225),
            random.nextInt(225),
        )
    } else {
        return@let ContextCompat.getColor(this, R.color.black)
    }
}

infix fun Context.toast(value: Any) {
    Toast.makeText(this, value.toString(), Toast.LENGTH_SHORT).show()
}

