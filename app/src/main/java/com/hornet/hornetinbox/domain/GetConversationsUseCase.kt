package com.hornet.hornetinbox.domain

import com.hornet.hornetinbox.DataFetcher
import com.hornet.hornetinbox.data.models.dto.Conversation
import com.hornet.hornetinbox.data.models.responses.ConversationsPageData
import kotlinx.serialization.json.*
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor() {

    operator fun invoke(page: Int): ArrayList<Conversation> {
        val conversationsDataJson = DataFetcher.getPage(page)

        conversationsDataJson.let {
            if (it.isNotEmpty()) {
                val conversationData = Json.decodeFromString(ConversationsPageData.serializer(), it)
                val conversations = conversationData.members.map { it }

                return ArrayList(conversations)
            } else
                return ArrayList()
        }
    }
}