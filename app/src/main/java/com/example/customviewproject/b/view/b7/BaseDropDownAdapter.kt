package com.example.customviewproject.b.view.b7

import android.view.View
import android.view.ViewGroup

/**
 *
 * @ClassName: BaseDropDownAdapter
 * @Author: 史大拿
 * @CreateDate: 9/15/22$ 3:15 PM$
 * TODO
 */
abstract class BaseDropDownAdapter {

    abstract fun count(): Int

    /*
     * 作者:史大拿
     * 创建时间: 9/15/22 3:16 PM
     * TODO 顶部tabView点击
     */
    abstract fun tabView(position: Int, parent: ViewGroup): View

    /*
     * 作者:史大拿
     * 创建时间: 9/15/22 3:16 PM
     * TODO 点击展示
     */
    abstract fun contentView(position: Int, parent: ViewGroup): View


    private val listObserved = arrayListOf<DropDownObserved>()

    fun register(observed: DropDownObserved) {
        listObserved.add(observed)
    }

    fun unregister(observed: DropDownObserved) {
        if (listObserved.contains(observed)) {
            listObserved.remove(observed)
        }
    }

    // 通过观察者模式 关闭
    open fun closeContent() {
        listObserved.forEach {
            it.closeContent()
        }
    }

    // 点击回调监听
    var blockClick: Click? = null

    interface Click {
        /*
         * 作者:史大拿
         * 创建时间: 9/15/22 5:42 PM
         * @param view: 当前选中的view
         * @param oldView: 上一个view
         * @param position:当前下标
         * @param oldPosition:上一个的下标
         */
        fun click(view: View, oldView: View?, position: Int, oldPosition: Int)

        fun openView(view: View, position: Int)

        fun closeView(view: View, position: Int)
    }
}