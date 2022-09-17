package com.hornet.hornetinbox.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.models.Inbox
import com.hornet.hornetinbox.data.repository.InboxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val inboxRepository: InboxRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
): ViewModel() {

    private var nextPageToLoad = 0

    var currentInboxesDisplayed: List<Inbox> = listOf()

    val initialState = InboxViewState(
        isLoading = true,
        hasError = false,
        hasLoadedMore = false,
        canLoadMore = false,
        data = emptyList(),
        hasUpdatedContent = false,
        errorMessage = null
    )

    private val _inboxFlow = MutableSharedFlow<InboxViewState>()
    val inboxFlow = _inboxFlow.asSharedFlow()

    fun initialise() {
        fetchInbox(nextPageToLoad)
    }

    fun loadMore(pageToLoad: Int = nextPageToLoad) {
        fetchInbox(pageToLoad = pageToLoad, isLoadingMore = true)
    }

    fun updateLastMessageTime() {
        ArrayList(currentInboxesDisplayed.toMutableList()).also { currentList ->
            val randomItem = currentList[Random.nextInt(1, currentList.lastIndex)]
            currentList.remove(randomItem)
            currentInboxesDisplayed = currentList.toList()
            updateCurrentInboxList(
                listOf(randomItem.copy(lastMessageDate = System.currentTimeMillis()))
            )
            emitNewState(getSuccessState(currentInboxesDisplayed, hasUpdatedContent = true))
        }
    }

    private fun emitNewState(newState: InboxViewState) {
        viewModelScope.launch {
            _inboxFlow.emit(newState)
        }
    }

    private fun fetchInbox(pageToLoad: Int, isLoadingMore: Boolean = false) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            runCatching {
                inboxRepository.getInbox(pageToLoad)
            }.onSuccess { inboxes ->
                updateCurrentInboxList(inboxesToBeAdded = inboxes)
                emitNewState(getSuccessState(currentInboxesDisplayed, hasLoadedMore = isLoadingMore))
                nextPageToLoad++
            }.onFailure { error ->
               emitNewState(getErrorState(error))
            }
        }
    }

    private fun updateCurrentInboxList(inboxesToBeAdded: List<Inbox>) {
        val updatedInboxes = currentInboxesDisplayed.toMutableList()
        updatedInboxes.addAll(inboxesToBeAdded)
        currentInboxesDisplayed = updatedInboxes
            .sortedWith(compareBy { it.lastMessageDate })
            .reversed()
            .toList()
    }

    private fun getSuccessState(
        inboxes: List<Inbox>,
        hasLoadedMore: Boolean = false,
        hasUpdatedContent: Boolean = false
    ): InboxViewState {
        return InboxViewState(
            data = inboxes,
            canLoadMore = canLoadMore(),
            hasLoadedMore = hasLoadedMore,
            isLoading = false,
            hasError = false,
            hasUpdatedContent = hasUpdatedContent,
            errorMessage = null
        )
    }

    private fun getErrorState(error: Throwable): InboxViewState {
        return InboxViewState(
            data = emptyList(),
            canLoadMore = false,
            hasLoadedMore = false,
            isLoading = false,
            hasError = true,
            hasUpdatedContent = false,
            errorMessage = error.message
        )
    }

    private fun canLoadMore() = nextPageToLoad < 4
}