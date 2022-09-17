package com.example.customviewproject.e.e1.view

/**
 *
 * @ClassName: BaseChatAdapter
 * @Author: 史大拿
 * @CreateDate: 9/17/22$ 1:49 PM$
 * TODO
 */
abstract class BaseChatAdapter {
    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 1:50 PM
     * TODO 填充样式
     */
    open fun fillStyle(): E1ChartView.FillStyle = E1ChartView.FillStyle.STROKE

    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 1:54 PM
     * TODO 原点样式
     */
    open fun pointStyle(): E1ChartView.PointStyle = E1ChartView.PointStyle.ROUND

    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 2:34 PM
     * TODO 绘制线样式
     */
    open fun lineStyle(): E1ChartView.LineStyle = E1ChartView.LineStyle.SOLID_LINE

    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 2:52 PM
     * TODO 是否绘制网格
     */
    open fun isDrawGrid() = true

    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 2:06 PM
     * TODO 水平个数 (左侧文字个数)
     */
    abstract fun horizontalCount(): Int

    /*
     * 作者:史大拿
     * 创建时间: 9/17/22 2:07 PM
     * TODO 垂直个数
     */
    abstract fun verticalCount(): Int
}