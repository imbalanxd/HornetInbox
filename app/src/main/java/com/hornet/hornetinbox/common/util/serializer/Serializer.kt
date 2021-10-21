package com.hornet.hornetinbox.common.util.serializer

interface Serializer {
    fun <T> deserializeObject(input: String?, outputType: Class<T>?): T?
}