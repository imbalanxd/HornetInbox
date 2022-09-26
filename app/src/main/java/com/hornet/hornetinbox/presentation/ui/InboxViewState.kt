package com.hornet.hornetinbox.presentation.ui

import com.hornet.hornetinbox.data.models.Inbox

data class InboxViewState(
    val isLoading: Boolean = true,
    val canLoadMore: Boolean = false,
    val hasLoadedMore: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val hasUpdatedContent: Boolean = false,
    val data: List<Inbox> = emptyList()
)
