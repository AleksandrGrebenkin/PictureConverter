package com.github.aleksandrgrebenkin.pictureconverter.mvp.presenter

import com.github.aleksandrgrebenkin.pictureconverter.mvp.model.converter.IConverter
import com.github.aleksandrgrebenkin.pictureconverter.mvp.model.converter.Image
import com.github.aleksandrgrebenkin.pictureconverter.mvp.view.MainView
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import java.io.File

class MainPresenter(
    private val uiScheduler: Scheduler,
    private val converter: IConverter
) : MvpPresenter<MainView>() {

    var conversationDisposable: Disposable? = null

    fun convertImage(byteArray: ByteArray) {
        viewState.showConversionDialog()
        conversationDisposable = converter.convertToPNG(byteArray)
            .observeOn(uiScheduler)
            .subscribe({
                viewState.hideConversionDialog()
                viewState.showSuccessResult()
            }, {
                viewState.hideConversionDialog()
                viewState.showErrorResult(it.message?: it.toString())
            }
            )
    }

    fun openImageClicked() {
        viewState.openImage()
    }

    fun conversionCancel() {
        conversationDisposable?.dispose()
        viewState.hideConversionDialog()
        viewState.showCancelResult()
    }
}