package com.cash.pinjaman.net.convertor

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.leopard.network.NetworkService
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
                val status = jsonObject.optString("status");
                val code = jsonObject.optInt("code")
                if (code != 200 && HttpResponse.SUCCESS != status) {
                    if (code == 400) {
                        val uri =
                            Uri.parse("yyjy://account/login")
                        NetworkService.ctx.startActivity(
                            Intent(Intent.ACTION_VIEW, uri).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                        NetworkService.ctx.sendBroadcast(Intent("com.yyjy.account.ACTION_LOGIN_INVALID"))
                        Toast.makeText(NetworkService.ctx, "请重新登录", Toast.LENGTH_SHORT).show()
                    }
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