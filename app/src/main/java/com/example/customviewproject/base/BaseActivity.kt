package com.example.customviewproject.base

import android.R.color
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.customviewproject.ext.statusBarHeight
import java.lang.reflect.ParameterizedType


/**
 *
 * @ClassName: BaseActivity
 * @Author: android 超级兵
 * @CreateDate: 1/6/22$ 2:19 PM$
 * TODO
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    companion object {
        const val TITLE = "title"
        const val SUBTITLE = "subtitle"
    }

    protected open lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //利用反射，调用指定ViewBinding中的inflate方法填充视图
        val type = javaClass.genericSuperclass
        binding = if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<*>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            method.invoke(null, layoutInflater) as VB
        } else {
            customViewBinDing() ?: return
        }

        setContentView(binding.root)

        /*
         * 作者:android 超级兵
         * 创建时间: 1/8/22 12:46 PM
         * TODO 设置actionBar
         */
        supportActionBar?.subtitle = intent?.getStringExtra(SUBTITLE)
        supportActionBar?.title = intent?.getStringExtra(TITLE)

        initView(savedInstanceState)
    }


    /*
     * 作者:android 超级兵
     * 创建时间: 1/25/22 10:17 AM
     * TODO 自定义ViewBinDing
     *  例如 : XXXBinding.inflate(layoutInflater)
     */
    open fun customViewBinDing(): VB? = null

    abstract fun initView(savedInstanceState: Bundle?)
}