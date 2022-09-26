package com.hornet.hornetinbox.data.repository

import com.hornet.hornetinbox.data.DataFetcher
import com.hornet.hornetinbox.data.models.Member
import com.hornet.hornetinbox.domain.repository.InboxRepository
import javax.inject.Inject

class InboxRepositoryImpl @Inject constructor (
    private val dataFetcher: DataFetcher,
): InboxRepository {
    override suspend fun getInbox(page: Int): List<Member> {
        return dataFetcher.getPage(page = page)
    }
}