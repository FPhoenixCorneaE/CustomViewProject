package com.example.customviewproject.d.view.d4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.customviewproject.R
import com.example.customviewproject.c.fragment.C3Fragment

/**
 *
 * @ClassName: BannerLoadFragment
 * @Author: 史大拿
 * @CreateDate: 10/27/22$ 11:20 AM$
 * TODO
 */
class BannerLoadFragment private constructor() : Fragment() {
    companion object {
        const val ID = "id"
        fun getInstance(@LayoutRes id: Int): BannerLoadFragment {
            return BannerLoadFragment().apply {
                arguments = bundleOf(ID to id)
            }
        }
    }

    private val layoutId by lazy {
        arguments?.getInt(ID) ?: 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }
}