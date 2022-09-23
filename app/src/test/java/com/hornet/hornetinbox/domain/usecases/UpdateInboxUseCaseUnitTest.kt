@file:OptIn(ExperimentalCoroutinesApi::class)

package com.hornet.hornetinbox.domain.usecases

import androidx.compose.ui.graphics.Color
import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.FakeDataFetcher
import com.hornet.hornetinbox.data.mapper.MemberToInboxMapper
import com.hornet.hornetinbox.data.models.Inbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UpdateInboxUseCaseUnitTest {
    private val mockDispatcherProvider: CoroutineDispatcherProvider = mock()

    @Test
    fun `it should return an empty list if empty data are passed`() {
        runTest {
            whenever(mockDispatcherProvider.default).thenReturn(Dispatchers.Unconfined)
            val result = UpdateInboxContentUseCase(mockDispatcherProvider).invoke(emptyList(), emptyList())
            Assert.assertTrue(result.isEmpty())
        }
    }

    @Test
    fun `it should return a list of properly sorted items`() {
        runTest {
            whenever(mockDispatcherProvider.default).thenReturn(Dispatchers.Unconfined)
            val fakeMembers = FakeDataFetcher.fakeMembers.subList(0, 3)
            val fakeResult: List<Inbox> = fakeMembers.map { MemberToInboxMapper().map(it) }
            var result: List<Inbox> = UpdateInboxContentUseCase(mockDispatcherProvider).invoke(fakeResult, emptyList<Inbox>())
            Assert.assertTrue(result.isNotEmpty())
            Assert.assertTrue(result.size == fakeResult.size)
            Assert.assertTrue(result.containsAll(fakeResult))

            val mockInbox = Inbox(
                userId = 200,
                userName = "Fred",
                userImageBackground = Color.Cyan,
                lastMessageDate = System.currentTimeMillis()
            )
            result = UpdateInboxContentUseCase(mockDispatcherProvider)
                .invoke(result, listOf(mockInbox))

            Assert.assertTrue(result.isNotEmpty())
            Assert.assertTrue(result.size == 4)
            Assert.assertTrue(result.containsAll(fakeResult + listOf(mockInbox)))
            Assert.assertTrue(result.first() == mockInbox)
        }
    }
}