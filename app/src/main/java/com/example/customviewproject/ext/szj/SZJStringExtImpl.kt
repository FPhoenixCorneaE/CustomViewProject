package com.example.customviewproject.ext.szj

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.example.customviewproject.base.SZJ


/**
 *
 * @ClassName: SZJStringExtImpl
 * @Author: android 超级兵
 * @CreateDate: 4/25/22$ 1:18 PM$
 * TODO
 */
class SZJStringExtImpl private constructor(private val str: String) : SZJ<String> {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SZJStringExtImpl? = null

        @Synchronized
        fun getInstance(t: String): SZJStringExtImpl {
            if (instance == null) {
                instance = SZJStringExtImpl(t)
            }
            return instance ?: SZJStringExtImpl(t)
        }
    }

    /*
     * 作者:史大拿
     * 创建时间: 8/2/22 5:16 PM
     * TODO
     */
    @SuppressLint("SimpleDateFormat")
    fun dateToStamp(): Long = let {
        val simpleDateFormat =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            } else {
                return -1
            }
        return simpleDateFormat.parse(str).time
    }
}