package com.hornet.hornetinbox.data

import com.hornet.hornetinbox.data.repository.InboxRepositoryImpl
import com.hornet.hornetinbox.data.mapper.MemberToChatMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class InboxRepositoryUnitTest {

    @Test
    fun `repository should fetch data correctly`() {
        val mockDataFetcher: DataFetcherImpl = mock()
        val repository = InboxRepositoryImpl(mockDataFetcher, MemberToChatMapper())
        runTest {
            whenever(repository.getInbox(100)).thenReturn(emptyList())
            Assert.assertEquals(repository.getInbox(100).size, 0)
            verify(mockDataFetcher, times(1)).getPage(100)
        }
    }

}