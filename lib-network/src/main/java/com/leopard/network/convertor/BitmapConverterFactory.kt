package com.cash.pinjaman.net.convertor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/*********************************************
 * @author  JokerFish
 * @date   2018-08-23
 * @description
 * @version v1.0
 **********************************************/
class BitmapConverterFactory : Converter.Factory {
    private constructor()

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return if (type === Bitmap::class.java) UserResponseConverter<Bitmap>(type) else super.responseBodyConverter(type, annotations, retrofit)
    }


    companion object {
        private var bitMapCoverterFactory: BitmapConverterFactory? = null
        fun create(): BitmapConverterFactory {
            if (bitMapCoverterFactory == null) {
                bitMapCoverterFactory = BitmapConverterFactory()
            }
            return bitMapCoverterFactory!!
        }
    }
}

class UserResponseConverter<T>(type: Type) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T {
        val bytes = value.bytes()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return bitmap as T
    }

}