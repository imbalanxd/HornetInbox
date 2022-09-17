package com.hornet.hornetinbox.ui

import com.hornet.hornetinbox.models.Inbox

data class InboxViewState(
    val isLoading: Boolean,
    val canLoadMore: Boolean,
    val hasLoadedMore: Boolean,
    val hasError: Boolean,
    val errorMessage: String?,
    val hasUpdatedContent: Boolean,
    val data: List<Inbox>
)
