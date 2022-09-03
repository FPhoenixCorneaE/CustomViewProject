package com.example.customviewproject.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.base.BaseLayoutActivity
import com.example.customviewproject.base.BaseTabActivityImpl

/**
 *
 * @ClassName: ActivityExt
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 5:44 PM$
 * TODO
 */
/**
 * 跳转具体页面
 */
fun <T : Activity> Activity.jumpActivity(
    view: View,
    clazz: Class<T>,
) {
    startActivity(
        Intent(this, clazz) title (view as? TextView)?.text.toString()
    )
}

/**
 * tabActivity
 */
fun Activity.jumpTabActivity(
    view: View,
    data: List<Pair<String, Int>>, // 标题 + 布局
    @Nullable
    @LayoutRes
    headLayout: Int = -1, // headView 可以不传
) {
    val keys = data.map { it.first }.toTypedArray()
    val values = data.map { it.second }.toIntArray()
    Intent(this, BaseTabActivityImpl::class.java).also {
        it.putExtras(
            bundleOf(
                BaseTabActivityImpl.KEY to keys
            )
        )
        it.putExtras(
            bundleOf(
                BaseTabActivityImpl.VALUE to values
            )
        )
        // headView
        it.putExtra(BaseTabActivityImpl.HEAD_VIEW_LAYOUT_ID, headLayout)

        it title (view as? TextView)?.text.toString()

        startActivity(it)
    }
}

/**
 * 只展示Layout
 */
fun Activity.jumpLayoutActivity(
    view: View,
    @LayoutRes layoutId: Int
) {
    Intent(this, BaseLayoutActivity::class.java).also {
        it.putExtra(BaseLayoutActivity.LAYOUT_ID, layoutId)

        it title (view as? TextView)?.text.toString()
        startActivity(it)
    }

}


infix fun Intent.title(name: String): Intent = let {
    it.putExtra(BaseActivity.TITLE, name)
        .putExtra(BaseActivity.SUBTITLE, "${it.component?.shortClassName}")
}