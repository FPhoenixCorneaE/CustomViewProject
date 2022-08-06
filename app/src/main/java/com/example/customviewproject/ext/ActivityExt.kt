package com.example.customviewproject.ext

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.example.customviewproject.base.BaseActivity

/**
 *
 * @ClassName: ActivityExt
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 5:44 PM$
 * TODO
 */
fun <T : Activity> Activity.jumpActivity(
    view: View,
    clazz: Class<T>,
) {
    startActivity(
        Intent(this, clazz) title (view as? TextView)?.text.toString()
    )
}

infix fun Intent.title(name: String): Intent = let {
    it.putExtra(BaseActivity.TITLE, name)
        .putExtra(BaseActivity.SUBTITLE, "${it.component?.shortClassName}")
}