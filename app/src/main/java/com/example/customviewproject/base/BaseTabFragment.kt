package com.example.customviewproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.jetbrains.annotations.NotNull

/**
 *
 * @ClassName: BaseTabFragment
 * @Author: 史大拿
 * @CreateDate: 8/29/22$ 3:23 PM$
 * TODO
 */
class BaseTabFragment private constructor() : Fragment() {

    companion object {
        @NotNull
        private const val LAYOUT_ID = "layout_id"

        fun instance(@LayoutRes layoutId: Int) = let {
            BaseTabFragment().apply {
                arguments = bundleOf(LAYOUT_ID to layoutId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(requireArguments().getInt(LAYOUT_ID), container, false)
    }


}