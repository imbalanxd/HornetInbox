package com.hornet.hornetinbox.domain.repository

import com.hornet.hornetinbox.data.models.Member

interface InboxRepository {
    suspend fun getInbox(page: Int): List<Member>
}