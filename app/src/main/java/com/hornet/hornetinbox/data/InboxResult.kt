package com.hornet.hornetinbox.data


sealed class InboxResult<out T> {
    class Success<out T>(val data: T, val canLoadMore: Boolean): InboxResult<T>()
    class Error(val error: String): InboxResult<Nothing>()
    object Loading : InboxResult<Nothing>()
}