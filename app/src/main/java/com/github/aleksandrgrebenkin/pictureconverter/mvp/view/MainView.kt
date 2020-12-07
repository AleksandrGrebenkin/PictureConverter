package com.github.aleksandrgrebenkin.pictureconverter.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.SingleState

@SingleState
interface MainView : MvpView {

    fun openImage()
    fun showConversionDialog()
    fun hideConversionDialog()
    fun showSuccessResult()
    fun showCancelResult()
    fun showErrorResult(text: String)

}