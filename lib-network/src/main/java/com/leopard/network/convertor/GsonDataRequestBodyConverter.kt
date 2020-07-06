package com.cash.pinjaman.net.convertor


import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

/*********************************************
 * Author  JokerFish
 * Create   2018-06-29
 * Description
 * Email fengzhengbiao@vcard100.com
 */
class GsonDataRequestBodyConverter<T> internal constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) :
    Converter<T, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(
            buffer.outputStream(),
            Charset.forName("UTF-8")
        )
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(
            "application/json; charset=UTF-8".toMediaTypeOrNull(),
            buffer.readByteString()
        )
    }

}