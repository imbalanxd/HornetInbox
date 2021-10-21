package com.hornet.hornetinbox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hornet.hornetinbox.common.util.Lists.RecentInboxArrayList
import com.hornet.hornetinbox.repo.IMailRepository
import com.hornet.hornetinbox.repo.MailRepository
import com.hornet.hornetinbox.repo.model.InboxPageModel
import com.hornet.hornetinbox.repo.model.MemberDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InboxViewModel : ViewModel() {
    private val mailRepository: IMailRepository = MailRepository()
    private var takenInitials = arrayListOf<String>()
    private var pageResult: InboxPageModel? = null
    private var allMailPagesResult = MutableLiveData<List<InboxPageModel?>>()
    private val inboxMembers = MutableLiveData<List<MemberDetailsModel>>()
    private val isListUpdate = MutableLiveData<Boolean>()
    private val recentInboxList = RecentInboxArrayList()
    private val allInboxList = arrayListOf<MemberDetailsModel>()
    private var recentPageAdded = -1

    fun getInboxMembers(): LiveData<List<MemberDetailsModel>> = inboxMembers
    fun getAllMailPagesResults(): LiveData<List<InboxPageModel?>> = allMailPagesResult
    fun getRecentPageAdded(): Int = recentPageAdded
    fun getRecentInboxList(): RecentInboxArrayList = recentInboxList
    fun getAllInboxList(): List<MemberDetailsModel> = allInboxList
    fun isListUpdate(): LiveData<Boolean> = isListUpdate

    fun getPage(pageNum: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result: InboxPageModel? = mailRepository.getPage(pageNum)
                if (result != null) {
                    pageResult = result
                    inboxMembers.postValue(result.members)
                }
            }
        }
    }

    fun getAllMailPages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result: List<InboxPageModel?> = mailRepository.getAllPages()
                if (result.isNotEmpty()) {
                    updateMembersInAllPages(result)
                    allMailPagesResult.postValue(result)
                    isListUpdate.postValue(true)
                }
            }
        }
    }

    fun sortMembersList(membersDetailsList: List<MemberDetailsModel>): List<MemberDetailsModel> {
        return membersDetailsList.sortedBy { it.last_message_at }.reversed()
    }

    fun getInboxPage(pageNum: Int): InboxPageModel? {
        return allMailPagesResult.value?.firstOrNull { page -> page?.page == pageNum }
    }

    fun setRecentPageAdded(recentPageAdded: Int) {
        this.recentPageAdded = recentPageAdded
    }

    fun addInboxMembersToRecentInboxList(members: List<MemberDetailsModel>) {
        members.forEach { member ->
            if (!recentInboxList.add(member)) {
                allInboxList.add(member)
            }
        }
    }

    private fun updateMembersInAllPages(pages: List<InboxPageModel?>) {
        pages.forEach { page ->
            page?.members?.let { members ->
                updateMemberDate(members)
                updateAllMembersProfileInitials(members)
            }
        }
    }

    private fun updateMemberDate(members: List<MemberDetailsModel>) {
        members.forEach { member ->
            member.last_message_at = member.last_message_at * 1000
        }
    }

    private fun updateAllMembersProfileInitials(members: List<MemberDetailsModel>) {
        members.forEach { member ->
            val initial = member.name?.let { name -> name[0].toString() } ?: ""

            if (takenInitials.contains(initial)) {
                member.profileInitial = initial + "-" + member.id
            } else {
                takenInitials.add(initial)
                member.profileInitial = initial
            }
        }
    }

    fun setAllInboxMemberList(pages: List<InboxPageModel?>) {
        pages.forEach { page ->
            page?.members?.let { members ->
                allInboxList.addAll(members)
            }
        }
    }

    fun updateRandomUserTime() {
        val randomUser = allInboxList.random()
        randomUser.last_message_at = System.currentTimeMillis()
        isListUpdate.postValue(true)
    }

    fun isInboxListUpdated(isListUpdate: Boolean) {
        this.isListUpdate.postValue(isListUpdate)
    }

    fun clearAllMailPagesResult() {
        allMailPagesResult.postValue(null)
        allInboxList.clear()
    }
}