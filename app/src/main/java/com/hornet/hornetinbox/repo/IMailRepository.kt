package com.hornet.hornetinbox.repo

import com.hornet.hornetinbox.repo.model.InboxPageModel

interface IMailRepository {
    fun getPage(pageNum: Int): InboxPageModel?
    fun getAllPages(): List<InboxPageModel?>
}