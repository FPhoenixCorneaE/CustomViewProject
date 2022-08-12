package com.example.customviewproject.a.activity

import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.animation.*
import android.widget.LinearLayout
import com.example.customviewproject.base.BaseActivity
import com.example.customviewproject.databinding.A6ActivityBinding
import com.example.customviewproject.ext.click
import kotlin.concurrent.thread

/**
 *
 * @ClassName: A6Activity
 * @Author: 史大拿
 * @CreateDate: 8/8/22$ 7:13 PM$
 * TODO camera使用
 */
class A6Activity : BaseActivity<A6ActivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        binding.btRotate.click {

//            animatorSet()

            // 属性动画
//           animator1()

            // PropertyValuesHolder
//            animator2()

            // 关键帧
//            keyFrame()

            // 插植器
            interpolator()
        }
    }

    //region interpolator 插值器
    /*
     * 作者:史大拿
     * 创建时间: 8/9/22 3:57 PM
     */
    private fun interpolator() {
        val animator =
            ObjectAnimator.ofFloat(binding.cameraView,
                "rotateAngle",
                10f,
                30f,
                90f,
                270f,
                360f,
                0f)

        //  AccelerateDecelerateInterpolator() 开始结束慢 中间快
        //  AccelerateInterpolator() 开始慢 后面快
        // DecelerateInterpolator() 开始快 后面慢
        // BounceInterpolator() 快结束的时候会反弹一下
        animator.interpolator = BounceInterpolator()

        animator.duration = 3000
        animator.start()
    }

    //endregion

    //region keyFrame 关键帧
    /*
     * 作者:史大拿
     * 创建时间: 8/9/22 3:49 PM
     */
    private fun keyFrame() {

        val max = 360f

        val k1 = Keyframe.ofFloat(0.1f, 0.1f * max) // * max 是为了计算对应的比例
        val k2 = Keyframe.ofFloat(0.3f, 0.6f * max)
        val k3 = Keyframe.ofFloat(1f, 1f * max)
        val propertyValuesHolder = PropertyValuesHolder.ofKeyframe("rotateAngle", k1, k2, k3)

        val animator =
            ObjectAnimator.ofPropertyValuesHolder(binding.cameraView, propertyValuesHolder)

        animator.duration = 3000
        animator.start()
    }
    //endregion

    //region PropertyValuesHolder 用来操作多个属性
    /*
     * 作者:史大拿
     * 创建时间: 8/9/22 3:42 PM
     */
    private fun animator2() {
        val p1 = PropertyValuesHolder.ofFloat("rotateAngle", 60f)
        val p2 = PropertyValuesHolder.ofFloat("rotateAngle", 80f)
        val p3 = PropertyValuesHolder.ofFloat("rotateAngle", 100f)
        val holder =
            ObjectAnimator.ofPropertyValuesHolder(binding.cameraView, p1, p2, p3)
        holder.duration = 2000
        holder.start()
    }

    //endregion

    //region // 属性动画
    /*
     * 作者:史大拿
     * 创建时间: 8/9/22 3:49 PM
     */

    fun animator1() {
        val animator =
            ObjectAnimator.ofFloat(binding.cameraView,
                "rotateAngle",
                10f,
                30f,
                90f,
                270f,
                360f,
                0f)

        animator.duration = 3000
        animator.start()
    }

    //endregion

    //region animatorSet
    /*
     * 作者:史大拿
     * 创建时间: 8/9/22 3:45 PM
     */
    private fun animatorSet() {
        val animator =
            ObjectAnimator.ofFloat(binding.cameraView,
                "rotateAngle",
                10f,
                30f,
                90f,
                270f,
                360f,
                0f)
        // animatorSet可以用于不同属性的依次执行
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator)
        animatorSet.start()
    }

    //endregion

}