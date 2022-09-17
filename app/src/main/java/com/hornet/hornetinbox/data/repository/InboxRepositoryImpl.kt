package com.hornet.hornetinbox.data.repository

import com.hornet.hornetinbox.data.DataFetcher
import com.hornet.hornetinbox.mapper.MemberToChatMapper
import com.hornet.hornetinbox.models.Inbox
import javax.inject.Inject

class InboxRepositoryImpl @Inject constructor (
    private val dataFetcher: DataFetcher,
    private val mapper: MemberToChatMapper
): InboxRepository {
    override suspend fun getInbox(page: Int): List<Inbox> {
        return dataFetcher.getPage(pageNum = page)
            .map { mapper.map(it) }
    }
}