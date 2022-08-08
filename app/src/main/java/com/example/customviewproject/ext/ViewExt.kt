package com.example.customviewproject.ext

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import com.example.customviewproject.R
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

// 获取通用图片
fun View.getBitMap(width: Int = 640): Bitmap = let {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, R.mipmap.user)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width
    BitmapFactory.decodeResource(resources, R.mipmap.user, options)
}