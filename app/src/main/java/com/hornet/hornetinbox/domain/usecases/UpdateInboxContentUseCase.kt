package com.hornet.hornetinbox.domain.usecases

import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.models.Inbox
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateInboxContentUseCase @Inject constructor (
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {
     suspend operator fun invoke (
         currentListOfInbox: List<Inbox>,
         inBoxesToBeAdded: List<Inbox>): List<Inbox> {
         return withContext(coroutineDispatcherProvider.default) {
             val updatedInboxes = currentListOfInbox.toMutableList()
             updatedInboxes.addAll(inBoxesToBeAdded)
             updatedInboxes.sortedByDescending { it.lastMessageDate }.toList()
         }
     }
}