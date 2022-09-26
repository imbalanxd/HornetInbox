@file:OptIn(ExperimentalCoroutinesApi::class)

package com.hornet.hornetinbox.domain.usecases

import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.FakeDataFetcher
import com.hornet.hornetinbox.data.mapper.MemberToInboxMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.*

class UpdateRandomInboxItemUseCaseUnitTest {
    private val mockDispatcher: CoroutineDispatcherProvider = mock()
    private val mockMembers = FakeDataFetcher.fakeMembers
    private val mockInboxes = mockMembers.map { MemberToInboxMapper().map(it) }

    @Test
    fun `should return an empty list if an empty list or a negative number is passed`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val updateInboxContentUseCase = UpdateInboxContentUseCase(mockDispatcher)
            val updateRandomInboxTimeUseCase = UpdateRandomInboxTimeUseCase(
                mockDispatcher,
                updateInboxContentUseCase
            )

            var result = updateRandomInboxTimeUseCase.invoke(emptyList(), System.currentTimeMillis())
            Assert.assertTrue(result.isEmpty())

            result = updateRandomInboxTimeUseCase.invoke(mockInboxes, -1238377383L)
            Assert.assertTrue(result.isEmpty())

            result = updateRandomInboxTimeUseCase.invoke(emptyList(), -1238377383L)
            Assert.assertTrue(result.isEmpty())
        }
    }

    @Test
    fun `should not modify size of the list`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val updateInboxContentUseCase = UpdateInboxContentUseCase(mockDispatcher)
            val updateRandomInboxTimeUseCase = UpdateRandomInboxTimeUseCase(
                mockDispatcher,
                updateInboxContentUseCase
            )

            val result = updateRandomInboxTimeUseCase.invoke(mockInboxes, System.currentTimeMillis())
            Assert.assertTrue(result.isNotEmpty())
            Assert.assertTrue(result.size == mockInboxes.size)

        }
    }

    @Test
    fun `should call UpdateInboxContentUseCase for sorting`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val updateInboxContentUseCase: UpdateInboxContentUseCase = mock()
            whenever(updateInboxContentUseCase.invoke(any(), any())).thenReturn(mockInboxes)
            val updateRandomInboxTimeUseCase = UpdateRandomInboxTimeUseCase(
                mockDispatcher,
                updateInboxContentUseCase
            )

            val result = updateRandomInboxTimeUseCase.invoke(mockInboxes, System.currentTimeMillis())
            Assert.assertTrue(result.isNotEmpty())
            Assert.assertTrue(result.size == mockInboxes.size)

            verify(updateInboxContentUseCase, times(1)).invoke(any(), any())
        }
    }

    @Test
    fun `should update a random item and sort appropriately`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val updateInboxContentUseCase = UpdateInboxContentUseCase(mockDispatcher)
            val updateRandomInboxTimeUseCase = UpdateRandomInboxTimeUseCase(
                mockDispatcher,
                updateInboxContentUseCase
            )

            val fakeTime = System.currentTimeMillis()

            val result = updateRandomInboxTimeUseCase.invoke(mockInboxes, fakeTime)
            Assert.assertTrue(result.isNotEmpty())
            Assert.assertTrue(result.size == mockInboxes.size)
            Assert.assertTrue(result.first().lastMessageDate == fakeTime)

        }
    }
}