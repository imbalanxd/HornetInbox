package com.hornet.hornetinbox.common.util.serializer

import com.google.gson.JsonSyntaxException
import com.google.gson.GsonBuilder
import java.lang.IllegalStateException

object GsonSerializer : Serializer {
    override fun <T> deserializeObject(input: String?, outputType: Class<T>?): T? {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return try {
            gson.fromJson(input, outputType)
        } catch (exception: IllegalStateException) {
            null
        } catch (exception: JsonSyntaxException) {
            null
        }
    }
}
