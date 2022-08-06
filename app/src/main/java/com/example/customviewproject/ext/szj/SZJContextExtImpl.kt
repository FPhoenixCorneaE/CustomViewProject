package com.example.customviewproject.ext.szj

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.example.customviewproject.R
import com.example.customviewproject.base.SZJ
import kotlin.random.Random

/**
 *
 * @ClassName: SZJContextExtImpl
 * @Author: android 超级兵
 * @CreateDate: 4/25/22$ 10:24 AM$
 * TODO
 */
open class SZJContextExtImpl private constructor(private val context: Context) : SZJ<Context> {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: SZJContextExtImpl? = null

        @Synchronized
        fun getInstance(t: Context): SZJContextExtImpl {
            if (instance == null) {
                instance = SZJContextExtImpl(t)
            }
            return instance ?: SZJContextExtImpl(t)
        }
    }

    /*
     * 作者:android 超级兵
     * 创建时间: 4/25/22 10:18 AM
     * TODO 获取样式
     */
    fun getTheme(@AttrRes attrId: Int): Int? = let {
        val typedValue = TypedValue()
        // 解析属性,解析成功范围true
        if (context.theme.resolveAttribute(attrId, typedValue, true)) {
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
    fun randomColor(@IntRange(from = 0, to = 255) alpha: Int = 255): Int = let {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val random = Random
            return@let Color.argb(
                alpha,
                random.nextInt(225),
                random.nextInt(225),
                random.nextInt(225),
            )
        } else {
            return@let ContextCompat.getColor(context, R.color.black)
        }
    }

    /*
     * 作者:android 超级兵
     * 创建时间: 1/7/22 5:27 PM
     * TODO float to dp
     */
    fun toDp(value: Float) = let {
        (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics
        ))
    }


    infix fun toast(value: Any) {
        Toast.makeText(context, value.toString(), Toast.LENGTH_SHORT).show()
    }


    // 屏幕的宽
    fun screenWidth() = let {
        context.resources.displayMetrics.widthPixels
    }

    // 屏幕的高
    fun screenHeight() = let {
        context.resources.displayMetrics.heightPixels
    }


}


