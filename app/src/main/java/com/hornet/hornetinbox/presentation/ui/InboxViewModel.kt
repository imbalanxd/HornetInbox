package com.hornet.hornetinbox.presentation.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hornet.hornetinbox.data.InboxResult
import com.hornet.hornetinbox.data.models.Inbox
import com.hornet.hornetinbox.domain.usecases.GetInboxUseCase
import com.hornet.hornetinbox.domain.usecases.UpdateInboxContentUseCase
import com.hornet.hornetinbox.domain.usecases.UpdateRandomInboxTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val getInboxUseCase: GetInboxUseCase,
    private val updateInboxContentUseCase: UpdateInboxContentUseCase,
    private val updateRandomInboxTimeUseCase: UpdateRandomInboxTimeUseCase
) : ViewModel() {

    @VisibleForTesting
    var nextPageToLoad = 0
        private set

    @VisibleForTesting
    var currentInboxesDisplayed: List<Inbox> = listOf()
        private set

    private val initialState = InboxViewState()

    private var _state = mutableStateOf(initialState)
    val inboxState = _state

    fun initialise() {
        fetchInbox(nextPageToLoad)
    }

    fun loadMore(pageToLoad: Int = nextPageToLoad) {
        fetchInbox(pageToLoad = pageToLoad, isLoadingMore = true)
    }

    fun updateLastMessageTime(newTime: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            currentInboxesDisplayed = updateRandomInboxTimeUseCase(
                inboxes = currentInboxesDisplayed,
                newTime = newTime
            )
            emitNewState(
                _state.value.copy(
                    isLoading = false,
                    data = currentInboxesDisplayed,
                    hasUpdatedContent = true,
                    hasLoadedMore = false
                )
            )
        }
    }

    private fun emitNewState(newState: InboxViewState) {
        _state.value = newState
    }

    private fun updateCurrentInboxList(
        inboxesToBeAdded: List<Inbox>,
        isLoadingMore: Boolean,
        canLoadMore: Boolean
    ) {
        viewModelScope.launch {
            currentInboxesDisplayed = updateInboxContentUseCase(
                currentListOfInbox = currentInboxesDisplayed,
                inBoxesToBeAdded = inboxesToBeAdded
            )
            emitNewState(
                InboxViewState(
                    isLoading = false,
                    data = currentInboxesDisplayed,
                    canLoadMore = canLoadMore,
                    hasLoadedMore = isLoadingMore
                )
            )
        }
    }

    private fun fetchInbox(pageToLoad: Int, isLoadingMore: Boolean = false) {
        viewModelScope.launch {
            getInboxUseCase(pageToLoad, isLoadingMore)
                .collect { inboxResult ->
                    when (inboxResult) {
                        is InboxResult.Loading -> emitNewState(InboxViewState())
                        is InboxResult.Success -> handleSuccess(
                            inboxResult.data,
                            inboxResult.canLoadMore,
                            isLoadingMore
                        )
                        is InboxResult.Error -> emitNewState(
                            InboxViewState(
                                isLoading = false,
                                hasError = true,
                                errorMessage = inboxResult.error
                            )
                        )
                    }
                }
        }
    }

    private fun handleSuccess(
        inboxList: List<Inbox>,
        canLoadMore: Boolean,
        isLoadingMore: Boolean
    ) {
        updateCurrentInboxList(inboxList, isLoadingMore = isLoadingMore, canLoadMore = canLoadMore)
        nextPageToLoad++
    }
}