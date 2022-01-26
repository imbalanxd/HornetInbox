package com.hornet.hornetinbox.data.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: Long,
    val name: String,
    @SerialName("last_message_at") var lastMessageAt: Long,
    var relativeTime: String = "",
    var initials: String = name.first().toString(),
    var initialsBackgroundColor: Int = 0
)