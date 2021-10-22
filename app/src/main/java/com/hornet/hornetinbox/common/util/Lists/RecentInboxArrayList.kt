package com.hornet.hornetinbox.common.util.Lists

import com.hornet.hornetinbox.repo.model.MemberDetailsModel

const val MAX_RECENT_INBOX_SIZE = 20

class RecentInboxArrayList : ArrayList<MemberDetailsModel?>() {
    override fun add(e: MemberDetailsModel?): Boolean {
        return if (this.size < MAX_RECENT_INBOX_SIZE) {
            super.add(e)
        } else false
    }
}