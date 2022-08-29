package com.example.customviewproject.c.fragment.c3

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.Fragment
import com.example.customviewproject.R
import com.example.customviewproject.c.view.c3.C3Bean
import com.example.customviewproject.system.SystemAppCompatViewInflater
import org.jetbrains.annotations.NotNull
import java.lang.RuntimeException

/**
 *
 * @ClassName: C3Fragment
 * @Author: 史大拿
 * @CreateDate: 8/25/22$ 4:33 PM$
 * TODO
 */
class C3Fragment private constructor() : Fragment(), LayoutInflater.Factory2 {
    companion object {
        @NotNull
        private const val LAYOUT_ID = "layout_id"

        fun instance(@IdRes layoutId: Int) = let {
            C3Fragment().apply {
                arguments = bundleOf(LAYOUT_ID to layoutId)
            }
        }
    }

    private val layoutId by lazy {
        arguments?.getInt(LAYOUT_ID) ?: -1
    }

    // 用来存放需要视差移动的view
    val list = arrayListOf<View>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val newInflater = inflater.cloneInContext(activity)
        LayoutInflaterCompat.setFactory2(newInflater, this)
        return newInflater.inflate(layoutId, container, false)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet,
    ): View? {

        Log.e("szjName", "=================$name====================")
        val data = C3Bean() // 定义变量存起来
        (0 until attrs.attributeCount).forEach {
            when (attrs.getAttributeName(it)) {
                "parallaxTransformInX" -> {
                    data.parallaxTransformInX = attrs.getAttributeValue(it).toFloat()
                }
                "parallaxTransformInY" -> {
                    data.parallaxTransformInY = attrs.getAttributeValue(it).toFloat()
                }
                "parallaxTransformOutX" -> {
                    data.parallaxTransformOutX = attrs.getAttributeValue(it).toFloat()
                }
                "parallaxTransformOutY" -> {
                    data.parallaxTransformOutY = attrs.getAttributeValue(it).toFloat()
                }
                "parallaxRotate" -> { // 旋转
                    data.parallaxRotate = attrs.getAttributeValue(it).toFloat()
                }
                "parallaxZoom" -> { // 缩放
                    data.parallaxZoom = attrs.getAttributeValue(it).toFloat()
                }

            }
            Log.i("szj属性",
                "key:${attrs.getAttributeName(it)}\tvalue:${attrs.getAttributeValue(it)}")
        }


        val view = createView(parent, name, context, attrs)

        // 自己创建的view!=null 并且 有设置属性 就存起来
        if (view != null && data.isNotEmpty()) {
            if (view.id != View.NO_ID) {
                view.setTag(view.id, data)
                list.add(view)
                Log.e("szj我自己创建",
                    "viewName:${view::class.java.simpleName}\ttag:${view.getTag(view.id)}")
            } else {
                throw RuntimeException("需要移动的view必须设置id来保证数据不会丢失..")
            }

        }
//        // 如果有一个属性存在 那么就算为需要视差的view


        return view
    }

    private var mAppCompatViewInflater = SystemAppCompatViewInflater()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }

    private fun createView(
        parent: View?, name: String?, mContext: Context,
        attrs: AttributeSet,
    ): View? {
        val is21 = Build.VERSION.SDK_INT < 21
        return mAppCompatViewInflater.createView(parent, name, mContext, attrs, false,
            is21,  /* Only read android:theme pre-L (L+ handles this anyway) */
            true,  /* Read read app:theme as a fallback at all times for legacy reasons */
            false /* Only tint wrap the context if enabled */
        )
    }


}