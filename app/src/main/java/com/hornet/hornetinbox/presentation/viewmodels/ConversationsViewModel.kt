package com.hornet.hornetinbox.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hornet.hornetinbox.data.models.dto.Conversation
import com.hornet.hornetinbox.domain.GetConversationsUseCase
import com.hornet.hornetinbox.utils.Constants.CONVERSATIONS_PER_PAGE
import com.hornet.hornetinbox.utils.Constants.MAX_RECENT_PAGE
import com.hornet.hornetinbox.utils.Constants.START_ARCHIVED_PAGE
import com.hornet.hornetinbox.utils.Constants.START_RECENT_PAGE
import com.hornet.hornetinbox.utils.Utils
import com.hornet.hornetinbox.utils.epochToTimeAgo
import com.hornet.hornetinbox.utils.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.random.Random

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val app: Application,
    private val getConversationsUseCase: GetConversationsUseCase
) :
    AndroidViewModel(app) {

    private val mostRecentConversationsEmitter = MutableLiveData<ArrayList<Conversation>>()
    val mostRecentConversations = mostRecentConversationsEmitter

    private val archivedConversationsEmitter = MutableLiveData<ArrayList<Conversation>>()
    val archivedConversations = archivedConversationsEmitter

    private val disableMostRecentLoadMoreEmitter = MutableLiveData<Boolean>()
    val disableMostRecentLoadMore = disableMostRecentLoadMoreEmitter

    private val disableArchivedLoadMoreEmitter = MutableLiveData<Boolean>()
    val disableArchivedLoadMore = disableArchivedLoadMoreEmitter

    private val refreshingEmitter = MutableLiveData<Boolean>()
    val refreshing = refreshingEmitter

    private val backgroundColorsHashSet: HashSet<String> = HashSet()

    private var mostRecentPage = START_RECENT_PAGE
    private var archivedPage = START_ARCHIVED_PAGE

    init {
        mostRecentConversationsEmitter.value = ArrayList()
        archivedConversationsEmitter.value = ArrayList()
        disableMostRecentLoadMoreEmitter.value = false
        disableArchivedLoadMoreEmitter.value = false
        refreshingEmitter.value = false
    }


    fun fetchMostRecentConversations() {
        val conversationList = getConversationsUseCase.invoke(mostRecentPage)

        if (mostRecentPage == MAX_RECENT_PAGE)
            disableMostRecentLoadMoreEmitter.value = true

        mostRecentPage++

        conversationList.forEach { conversation ->
            conversation.relativeTime =
                conversation.lastMessageAt.epochToTimeAgo(getApplication())
            conversation.initialsBackgroundColor =
                createBackgroundColorForInitials(conversation.initials)
        }

        mostRecentConversationsEmitter.value?.let {
            mostRecentConversationsEmitter.value?.addAll(conversationList)
            sortMostRecent()
            mostRecentConversationsEmitter.notifyObserver()
        }
    }

    fun fetchArchivedConversations() {
        val conversationList = getConversationsUseCase.invoke(archivedPage)
        archivedPage++

        if (conversationList.size < CONVERSATIONS_PER_PAGE)
            disableArchivedLoadMoreEmitter.value = true

        conversationList.forEach { conversation ->
            conversation.relativeTime =
                conversation.lastMessageAt.epochToTimeAgo(getApplication())
            conversation.initialsBackgroundColor =
                createBackgroundColorForInitials(conversation.initials)
        }

        archivedConversationsEmitter.value?.let {
            archivedConversationsEmitter.value?.addAll(conversationList)
            sortArchived()
            archivedConversationsEmitter.notifyObserver()
        }
    }

    fun updateRandomConversation() {
        val allConversations = arrayListOf<Conversation>()
        mostRecentConversationsEmitter.value?.let { allConversations.addAll(it) }
        archivedConversationsEmitter.value?.let { allConversations.addAll(it) }

        val randomIndex = Random.nextInt(allConversations.size - 1)
        val updatedConversation = allConversations[randomIndex]

        updatedConversation.lastMessageAt = System.currentTimeMillis() / 1000L
        updatedConversation.relativeTime =
            updatedConversation.lastMessageAt.epochToTimeAgo(getApplication())

        val mostRecentConversation =
            mostRecentConversationsEmitter.value?.find { it.id == updatedConversation.id }

        mostRecentConversation?.let {
            updateRelativeTimesForMostRecent()
            sortMostRecent()
            mostRecentConversationsEmitter.notifyObserver()
        } ?: kotlin.run {

            val mostRecentLastConversation = mostRecentConversationsEmitter.value?.last()
            mostRecentLastConversation?.let {
                archivedConversationsEmitter.value?.add(it)
                mostRecentConversationsEmitter.value?.remove(it)
                updateRelativeTimesForArchived()
                sortArchived()
            }

            val archivedConversation =
                archivedConversationsEmitter.value?.find { it.id == updatedConversation.id }
            archivedConversation?.let {
                mostRecentConversationsEmitter.value?.add(it)
                archivedConversationsEmitter.value?.remove(archivedConversation)
                updateRelativeTimesForMostRecent()
                sortMostRecent()
            }

            archivedConversationsEmitter.notifyObserver()
            mostRecentConversationsEmitter.notifyObserver()
        }
    }

    fun refreshInbox() {
        refreshingEmitter.value = true

        mostRecentPage = START_RECENT_PAGE
        archivedPage = START_ARCHIVED_PAGE

        mostRecentConversationsEmitter.value?.clear()
        archivedConversationsEmitter.value?.clear()
        disableMostRecentLoadMoreEmitter.value = false
        disableArchivedLoadMoreEmitter.value = false
        backgroundColorsHashSet.clear()

        fetchArchivedConversations()
        fetchMostRecentConversations()

        refreshingEmitter.value = false
    }

    private fun createBackgroundColorForInitials(initials: String): Int {
        val bckColor = Utils.randomColor
        val bckColorName = initials.plus(bckColor)

        while (true) {
            if (backgroundColorsHashSet.add(bckColorName))
                break
            else
                createBackgroundColorForInitials(initials)
        }

        return bckColor
    }

    private fun updateRelativeTimesForMostRecent() {
        mostRecentConversationsEmitter.value?.forEach {
            it.relativeTime = it.lastMessageAt.epochToTimeAgo(getApplication())
        }
    }

    private fun updateRelativeTimesForArchived() {
        archivedConversationsEmitter.value?.forEach {
            it.relativeTime = it.lastMessageAt.epochToTimeAgo(getApplication())
        }
    }

    private fun sortMostRecent() =
        mostRecentConversationsEmitter.value?.sortByDescending { it.lastMessageAt }

    private fun sortArchived() {
        archivedConversationsEmitter.value?.sortByDescending { it.lastMessageAt }
    }


}