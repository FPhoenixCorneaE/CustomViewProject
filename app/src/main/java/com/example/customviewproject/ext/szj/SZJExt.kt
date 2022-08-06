package com.example.customviewproject.ext.szj

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity


/**
 *
 * @ClassName: SZJExt
 * @Author: 史大拿
 * TODO
 */

/*
 * 作者:史大拿
 * 创建时间: 4/25/22 11:05 AM
 * TODO 添加域名【szj】
 */

/// contentExt
val Context.szj: SZJContextExtImpl
    get() = SZJContextExtImpl.getInstance(this)

/// stringExt
val String.szj: SZJStringExtImpl
    get() = SZJStringExtImpl.getInstance(this)

/// viewExt
val View.szj: SZJViewExtImpl
    get() = SZJViewExtImpl.getInstance(this)

/// activityExt
val AppCompatActivity.szj: SZJActivityExpImpl
    get() = SZJActivityExpImpl.getInstance(this)