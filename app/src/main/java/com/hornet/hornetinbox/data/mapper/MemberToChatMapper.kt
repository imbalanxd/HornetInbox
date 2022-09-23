package com.hornet.hornetinbox.data.mapper

import com.hornet.hornetinbox.Utils
import com.hornet.hornetinbox.data.models.Inbox
import com.hornet.hornetinbox.data.models.Member
import javax.inject.Inject

class MemberToChatMapper @Inject constructor() : Mapper<Member, Inbox> {
    override fun map(input: Member): Inbox {
        return Inbox(
            userId = input.id,
            userName = input.name,
            userImageBackground = Utils.generateBackgroundColor(),
            lastMessageDate = Utils.parseToLong(input.lastMessage)
        )
    }
}