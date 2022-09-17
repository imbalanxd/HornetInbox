package com.hornet.hornetinbox.data.repository

import com.hornet.hornetinbox.models.Inbox

interface InboxRepository {
    suspend fun getInbox(page: Int): List<Inbox>
}