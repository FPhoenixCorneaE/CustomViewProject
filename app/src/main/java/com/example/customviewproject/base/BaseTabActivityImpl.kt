package com.example.customviewproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
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

        @Nullable
        const val HEAD_VIEW_LAYOUT_ID = "head_view_layout_id"

        const val DEFAULT_VALUE = -1
    }

    private val pairKey by lazy {
        intent.extras?.getStringArray(KEY)
    }
    private val pairValue by lazy {
        intent.extras?.getIntArray(VALUE)
    }

    private val headLayoutId by lazy {
        intent.getIntExtra(HEAD_VIEW_LAYOUT_ID, DEFAULT_VALUE)
    }

    override fun initHeadView(vp: ViewGroup) {
        if (headLayoutId != DEFAULT_VALUE) {
            LayoutInflater.from(this).inflate(headLayoutId, vp, true)
        }
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