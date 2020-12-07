package com.github.aleksandrgrebenkin.pictureconverter.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

object Converter {
    fun convertToPNG(file: File): Completable = Completable.fromCallable {
        BitmapFactory.decodeFile(file.absolutePath).let { source ->
            val outputStream = FileOutputStream(file)
            source.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        }
    }.subscribeOn(Schedulers.computation())
}
