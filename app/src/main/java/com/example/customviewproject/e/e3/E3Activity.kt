package com.example.customviewproject.e.e3

import android.os.Bundle
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.E3ActivityBinding

/**
 *
 * @ClassName: E3Activity
 * @Author: 史大拿
 * @CreateDate: 9/21/22$ 3:52 PM$
 * TODO
 */
class E3Activity : BaseActivity<E3ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.polygonChartView.data = arrayListOf(
            "技术/职能型" to 6f,
            "综合管理型" to 2.5f,
            "自主/独立型" to 4f,
            "安全/稳定型" to 4f,
            "创业创新型" to 3f,
            "服务/奉献型" to 6f,
            "纯粹挑战型" to 2f,
            "生活方式型" to 5f,
        )
    }
}