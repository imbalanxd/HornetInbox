package com.hornet.hornetinbox.repo

import com.hornet.hornetinbox.DataFetcher
import com.hornet.hornetinbox.common.util.serializer.GsonSerializer
import com.hornet.hornetinbox.repo.model.InboxPageModel

class MailRepository : IMailRepository {

    @Deprecated("pageNum num not considered", ReplaceWith("com.hornet.hornetinbox.viewmodel.InboxViewModel.getInboxPage(pageNum: Int))"))
    override fun getPage(pageNum: Int): InboxPageModel? {
        val requiredPage = DataFetcher.getPage(pageNum)
        val page = GsonSerializer.deserializeObject(requiredPage, InboxPageModel::class.java)

        return page
    }

    override fun getAllPages(): List<InboxPageModel?> {
        val pages = DataFetcher.getAllPages()
        return convertPagesToInboxModelList(pages)
    }

    private fun convertPagesToInboxModelList(pages: List<String>): List<InboxPageModel?> {
        val inboxPageModelList  = arrayListOf<InboxPageModel?>()
        var inboxPageModel : InboxPageModel?

        pages.forEach { page ->
            inboxPageModel = GsonSerializer.deserializeObject(page, InboxPageModel::class.java)
            inboxPageModelList.add(inboxPageModel)
        }

        return inboxPageModelList
    }
}