package com.hornet.hornetinbox.domain

import com.hornet.hornetinbox.data.FakeDataFetcher
import com.hornet.hornetinbox.data.repository.InboxRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InboxRepositoryUnitTest {

    @Test
    fun `repository should fetch data correctly`() {
        val repository = InboxRepositoryImpl(FakeDataFetcher())
        runTest {
            val members = repository.getInbox(0)
            Assert.assertTrue(members.isNotEmpty())
            Assert.assertTrue(members.size == FakeDataFetcher.fakeMembers.size)
            Assert.assertTrue(members.all { it.id != 0 })
            Assert.assertTrue(members.all { it.name.isNotEmpty() })
        }
    }

}