package com.example.customviewproject.ext

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 *
 * @ClassName: ViewExt
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 5:44 PM$
 * TODO
 */
/*
     * 作者:史大拿
     * 创建时间: 8/5/22 9:31 AM
     * TODO 利用RxView 实现重复点击防抖动
     */
fun View.click(
    // 时间[单位:秒(s)]
    time: Int = 1,
    click: (View) -> Unit,
) {
    RxView.clicks(this).throttleFirst(time.toLong(), TimeUnit.SECONDS)
        .subscribe(object : io.reactivex.Observer<Any> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(value: Any) {
                click(this@click)
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        })
}