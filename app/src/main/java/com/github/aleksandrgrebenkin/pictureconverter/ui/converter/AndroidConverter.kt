package com.github.aleksandrgrebenkin.pictureconverter.ui.converter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.aleksandrgrebenkin.pictureconverter.mvp.model.converter.IConverter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.lang.RuntimeException

class AndroidConverter(private val context: Context) : IConverter {
    override fun convertToPNG(byteArray: ByteArray): Completable = Completable.fromAction {
        context.let {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                return@let
            }

            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val destFile = File(context.getExternalFilesDir(null), "destFileName.png")
            FileOutputStream(destFile).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }
    }.subscribeOn(Schedulers.io())
}