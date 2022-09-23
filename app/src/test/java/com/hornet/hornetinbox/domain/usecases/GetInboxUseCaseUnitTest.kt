@file:OptIn(ExperimentalCoroutinesApi::class)

package com.hornet.hornetinbox.domain.usecases

import android.content.res.Resources
import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.FakeDataFetcher
import com.hornet.hornetinbox.data.InboxResult
import com.hornet.hornetinbox.data.mapper.MemberToInboxMapper
import com.hornet.hornetinbox.data.models.Member
import com.hornet.hornetinbox.domain.repository.InboxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetInboxUseCaseUnitTest {
    private val mockDispatcher: CoroutineDispatcherProvider = mock()

    @Test
    fun `it should first emit a loading state`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val repository = object: InboxRepository {
                override suspend fun getInbox(page: Int): List<Member> {
                    return emptyList()
                }
            }

            val getInboxUseCase = GetInboxUseCase(repository, MemberToInboxMapper())
            val result = getInboxUseCase.invoke(0, false).first()
            Assert.assertTrue(result is InboxResult.Loading)
        }
    }

    @Test
    fun `it should emit correct data if all is successful`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val repository = object: InboxRepository {
                override suspend fun getInbox(page: Int): List<Member> {
                    return FakeDataFetcher.fakeMembers
                }
            }

            val getInboxUseCase = GetInboxUseCase(repository, MemberToInboxMapper())
            val result = getInboxUseCase.invoke(0, false).last()
            Assert.assertTrue(result is InboxResult.Success)
            val successResponse = result as InboxResult.Success
            val successData = successResponse.data
            Assert.assertTrue(successData.isNotEmpty())
            Assert.assertTrue(successData.size == FakeDataFetcher.fakeMembers.size)
            Assert.assertTrue(successResponse.canLoadMore)
        }
    }


    @Test
    fun `it should emit an error when error occurs in the repository`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val repository = object: InboxRepository {
                override suspend fun getInbox(page: Int): List<Member> {
                    throw Resources.NotFoundException()
                }
            }

            val getInboxUseCase = GetInboxUseCase(repository, MemberToInboxMapper())
            val result = getInboxUseCase.invoke(0, false).last()
            Assert.assertTrue(result is InboxResult.Error)
            Assert.assertTrue((result as InboxResult.Error).error == "Oh damn, we messed up :(")
        }
    }

    @Test
    fun `it should call Mapper and map correctly`() {
        runTest {
            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            val repository: InboxRepository = mock()
            whenever(repository.getInbox(0)).thenReturn(FakeDataFetcher.fakeMembers)

            val mapper: MemberToInboxMapper = mock()

            val getInboxUseCase = GetInboxUseCase(repository, mapper)
            val result = getInboxUseCase.invoke(0, false).last()

            Assert.assertTrue(result is InboxResult.Success)
            verify(repository, times(1)).getInbox(0)
            verify(mapper, times(1)).map(FakeDataFetcher.fakeMembers.first())
        }
    }
}