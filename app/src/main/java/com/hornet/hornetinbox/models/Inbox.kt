package com.hornet.hornetinbox.models

import androidx.compose.ui.graphics.Color

data class Inbox(
    val userId: Int,
    val userName: String,
    val userImageBackground: Color,
    val lastMessageDate: Long
)
