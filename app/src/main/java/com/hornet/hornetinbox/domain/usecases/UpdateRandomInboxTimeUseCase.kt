package com.hornet.hornetinbox.domain.usecases

import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.models.Inbox
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class UpdateRandomInboxTimeUseCase @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val updateInboxContentUseCase: UpdateInboxContentUseCase
) {
    suspend operator fun invoke(inboxes: List<Inbox>, newTime: Long): List<Inbox> {
        if (inboxes.isEmpty() || newTime < 1) return emptyList()
        return withContext(coroutineDispatcherProvider.default) {
            val copyOfCurrentList = inboxes.toMutableList()
            val randomInbox = copyOfCurrentList[Random.nextInt(0, inboxes.lastIndex)]
            copyOfCurrentList.remove(randomInbox)
            val updateInboxItem = randomInbox.copy(lastMessageDate = newTime)
            updateInboxContentUseCase(copyOfCurrentList, listOf(updateInboxItem))
        }
    }
}