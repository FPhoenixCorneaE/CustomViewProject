package com.example.customviewproject.ext.szj

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.base.SZJ

/**
 *
 * @ClassName: SZJActivityExpImpl
 * @Author: 史大拿
 * @CreateDate: 8/5/22$ 9:38 AM$
 * TODO
 */
class SZJActivityExpImpl private constructor(private val activity: AppCompatActivity) :
    SZJ<AppCompatActivity> {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SZJActivityExpImpl? = null

        @Synchronized
        fun getInstance(activity: AppCompatActivity): SZJActivityExpImpl {
            if (instance == null) {
                instance = SZJActivityExpImpl(activity)
            }
            return instance ?: SZJActivityExpImpl(activity)
        }
    }

    fun <T : Activity> jumpActivity(
        view: View,
        clazz: Class<T>,
    ) {
        activity.startActivity(
            Intent(activity, clazz) title (view as? TextView)?.text.toString()
        )
    }
}

infix fun Intent.title(name: String): Intent = let {
    it.putExtra(BaseActivity.TITLE, name)
        .putExtra(BaseActivity.SUBTITLE, "${it.component?.shortClassName}")
}