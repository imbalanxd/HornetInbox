package com.hornet.hornetinbox.mapper

import com.hornet.hornetinbox.Utils
import com.hornet.hornetinbox.models.Inbox
import com.hornet.hornetinbox.models.Member
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