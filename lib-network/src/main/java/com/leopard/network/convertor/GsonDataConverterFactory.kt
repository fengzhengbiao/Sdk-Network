package com.cash.pinjaman.net.convertor

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/*********************************************
 * Author  JokerFish
 * Create   2018-06-29
 * Description
 * Email fengzhengbiao@vcard100.com
 */
class GsonDataConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonDataResponseBodyConverter<Any>(gson, adapter as TypeAdapter<Any>, type)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonDataRequestBodyConverter<Any>(gson, adapter as TypeAdapter<Any>)
    }

    companion object {
        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        @JvmOverloads  // Guarding public API nullability.
        fun create(gson: Gson? = Gson()): GsonDataConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return GsonDataConverterFactory(gson)
        }
    }

}