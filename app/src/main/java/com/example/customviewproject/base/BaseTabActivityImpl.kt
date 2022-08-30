package com.example.customviewproject.base

import android.os.Bundle
import android.util.Log
import com.example.customviewproject.R
import org.jetbrains.annotations.NotNull

/**
 *
 * @ClassName: BaseTabActivityImpl
 * @Author: 史大拿
 * @CreateDate: 8/29/22$ 4:36 PM$
 * TODO
 */
class BaseTabActivityImpl : BaseTabActivity() {

    companion object {
        @NotNull
        const val KEY = "key"

        @NotNull
        const val VALUE = "value"
    }

    private val pairKey by lazy {
        intent.extras?.getStringArray(KEY)
    }
    private val pairValue by lazy {
        intent.extras?.getIntArray(VALUE)
    }

    override fun initData(): List<Pair<String, Int>> = let {
        val list = arrayListOf<Pair<String, Int>>()
        (pairKey?.indices)?.map {
            list.add(pairKey!![it] to pairValue!![it])
        }
        list
    }

    override fun initView(savedInstanceState: Bundle?) {
    }
}