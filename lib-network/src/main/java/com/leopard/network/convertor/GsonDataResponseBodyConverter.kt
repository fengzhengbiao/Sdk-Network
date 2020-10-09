package com.cash.pinjaman.net.convertor

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.leopard.network.entity.ApiException
import com.leopard.network.entity.HttpResponse
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException
import java.io.StringReader
import java.lang.reflect.*
import java.lang.reflect.Array

/*********************************************
 * Author  JokerFish
 * Create   2018-06-29
 * Description
 */
internal class GsonDataResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>,
    private val type: Type
) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        return try {
            val aClass = getRawType(type)
            if (aClass.isAssignableFrom(HttpResponse::class.java)) {
                val jsonReader = gson.newJsonReader(value.charStream())
                adapter.read(jsonReader)
            } else {
                val string = value.string()
                val jsonObject = JSONObject(string)
                val code = jsonObject.getInt("code")
                if (code != 200) {
                    val message = jsonObject.optString("Server Error!")
                    throw ApiException(message, 1000)
                }
                val data = jsonObject.optString("data")
                val jsonReader =
                    JsonReader(StringReader(data))
                jsonReader.isLenient = true
                adapter.read(jsonReader)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            val jsonReader =
                gson.newJsonReader(value.charStream())
            adapter.read(jsonReader)
        } finally {
            value.close()
        }
    }

    private fun getRawType(type: Type): Class<*> {
        if (type is Class<*>) {
            // Type is a normal class.
            return type
        }
        if (type is ParameterizedType) {

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            val rawType = type.rawType
            require(rawType is Class<*>)
            return rawType
        }
        if (type is GenericArrayType) {
            val componentType =
                type.genericComponentType
            return Array.newInstance(getRawType(componentType), 0).javaClass
        }
        if (type is TypeVariable<*>) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Any::class.java
        }
        if (type is WildcardType) {
            return getRawType(type.upperBounds[0])
        }
        throw IllegalArgumentException(
            "Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + type.javaClass.name
        )
    }

}