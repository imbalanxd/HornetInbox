package com.hornet.hornetinbox.data.mapper

interface Mapper<I,O> {
    fun map(input: I): O
}