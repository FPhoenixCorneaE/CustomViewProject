package com.example.customviewproject.a

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import com.example.customviewproject.R
import com.example.customviewproject.a.activity.*
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.AActivityBinding
import com.example.customviewproject.ext.click
import com.example.customviewproject.ext.jumpActivity
import com.example.customviewproject.ext.jumpTabActivity

/**
 *
 * @ClassName: AActivity
 * @Author: 史大拿
 * @CreateDate: 8/6/22$ 5:29 PM$
 * TODO 入门系列
 */
class AActivity : BaseActivity<AActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        // 可参考文档: https://juejin.cn/post/6844903487570968584

        binding.bt0.click {
            jumpActivity(it, A0Activity::class.java)
        }

        binding.bt1.click {
            jumpActivity(it, A1Activity::class.java)
        }

        binding.bt2.click {
            jumpActivity(it, A2Activity::class.java)
        }

        binding.bt3.click {
            jumpActivity(it, A3Activity::class.java)
        }

        binding.bt4.click {
            jumpActivity(it, A4Activity::class.java)
        }

        binding.bt5.click {
            jumpActivity(it, A5Activity::class.java)
        }

        binding.bt6.click {
            jumpActivity(it, A6Activity::class.java)
        }

        binding.bt7.click {
            jumpActivity(it, A7Activity::class.java)
        }

        binding.bt8.click {
            jumpTabActivity(
                listOf(
                    "CornerPathEffect" to R.layout.corner_path_effect,
                    "DiscretePathEffect" to R.layout.discrete_path_effect,
                    "DashPathEffect" to R.layout.dash_path_effect,
                    "PathDashPathEffect" to R.layout.path_dash_path_effect,
                    "SumPathEffect" to R.layout.sum_path_effect,
                    "ComposePathEffect" to R.layout.compose_path_effect,
                ),
                R.layout.path_effect_head
            )
        }
    }
}