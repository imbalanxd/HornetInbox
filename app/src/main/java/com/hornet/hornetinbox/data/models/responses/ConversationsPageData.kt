package com.hornet.hornetinbox.data.models.responses

import com.hornet.hornetinbox.data.models.dto.Conversation
import kotlinx.serialization.Serializable

@Serializable
data class ConversationsPageData(val page: Int, val members: MutableList<Conversation>)