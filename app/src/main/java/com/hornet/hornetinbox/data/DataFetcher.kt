package com.hornet.hornetinbox.data

import com.hornet.hornetinbox.data.models.Member

interface DataFetcher {
    suspend fun getPage(page: Int): List<Member>
}