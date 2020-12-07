package com.github.aleksandrgrebenkin.pictureconverter.mvp.model.converter

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convertToPNG(byteArray: ByteArray): Completable
}