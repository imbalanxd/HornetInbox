package com.hornet.hornetinbox.mapper

interface Mapper<I,O> {
    fun map(input: I): O
}