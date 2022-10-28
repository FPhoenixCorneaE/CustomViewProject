package com.example.customviewproject.d.view.d4

import androidx.fragment.app.FragmentManager

/**
 *
 * @ClassName: BannerLoadAdapterImpl
 * @Author: 史大拿
 * @CreateDate: 10/28/22$ 11:18 AM$
 * TODO
 */
class BannerLoadAdapter(
    private val fragmentManager: FragmentManager,
    private val listId: List<Int>,
    private val loadId: Int
) : BaseBannerLoadAdapter() {
    override fun getFragmentManager() = fragmentManager

    override fun getListLayoutId() = listId

    override fun loadId() = loadId
}