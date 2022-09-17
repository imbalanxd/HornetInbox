package com.hornet.hornetinbox.models

import com.google.gson.annotations.SerializedName

data class Member(
    val id: Int,
    val name: String,
    @SerializedName("last_message_at")
    val lastMessage: String
)

data class MemberResponse(val page: Int, val members: List<Member>)
