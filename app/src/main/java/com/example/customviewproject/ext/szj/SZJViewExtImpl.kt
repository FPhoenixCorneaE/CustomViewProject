package com.example.customviewproject.ext.szj

import android.annotation.SuppressLint
import android.view.View
import com.example.customviewproject.base.SZJ
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 *
 * @ClassName: SZJViewExtImpl
 * @Author: 史大拿
 * @CreateDate: 8/5/22$ 9:30 AM$
 * TODO
 */
class SZJViewExtImpl private constructor(val view: View) : SZJ<View> {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SZJViewExtImpl? = null

        @Synchronized
        fun getInstance(view: View): SZJViewExtImpl {
            if (instance == null) {
                instance = SZJViewExtImpl(view)
            }
            return instance ?: SZJViewExtImpl(view)
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 8/5/22 9:31 AM
     * TODO 利用RxView 实现重复点击防抖动
     */
    fun click(
        // 时间[单位:秒(s)]
        time: Int = 1,
        click: (View) -> Unit,
    ) {
        RxView.clicks(view).throttleFirst(time.toLong(), TimeUnit.SECONDS)
            .subscribe(object : io.reactivex.Observer<Any> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(value: Any) {
                    click(view)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }
            })
    }
}