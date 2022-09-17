package com.hornet.hornetinbox.viewModel

import androidx.compose.ui.graphics.Color
import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.repository.InboxRepositoryImpl
import com.hornet.hornetinbox.models.Inbox
import com.hornet.hornetinbox.ui.InboxViewModel
import com.hornet.hornetinbox.ui.InboxViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HornetInboxViewModelUnitTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockRepository: InboxRepositoryImpl = mock()
    private val mockDispatcher: CoroutineDispatcherProvider = mock()

    private val mockTimeInMillis = System.currentTimeMillis()
    private val mockInbox = Inbox(
        userId = 100,
        userName = "Robert",
        userImageBackground = Color.Black,
        lastMessageDate = mockTimeInMillis
    )

    private val mockInboxes = listOf(mockInbox)

    @Test
    fun `viewModel should set initial state correctly`() {
        val viewModel = InboxViewModel(
            inboxRepository = mockRepository,
            coroutineDispatcherProvider = mockDispatcher
        )

        Assert.assertFalse(viewModel.initialState.hasError)
        Assert.assertFalse(viewModel.initialState.hasLoadedMore)
        Assert.assertFalse(viewModel.initialState.canLoadMore)
        Assert.assertTrue(viewModel.initialState.data.isEmpty())
        Assert.assertNull(viewModel.initialState.errorMessage)

        runTest {
            verify(mockDispatcher, never()).io
            verify(mockRepository, never()).getInbox(0)
        }
    }

    @Test
    fun `initialise method should fetch first page of data and set correct state`() {
        runTest {
            val mockResult = emptyList<Inbox>()
            whenever(mockDispatcher.io).thenReturn(Dispatchers.Unconfined)
            whenever(mockRepository.getInbox(0)).thenReturn(mockResult)

            val viewModel = InboxViewModel(
                inboxRepository = mockRepository,
                coroutineDispatcherProvider = mockDispatcher
            )

            val successState = InboxViewState(
                data = mockResult,
                canLoadMore = true,
                hasLoadedMore = false,
                isLoading = false,
                hasError = false,
                hasUpdatedContent = false,
                errorMessage = null
            )
            viewModel.initialise()

            val viewModelCurrentState = viewModel.inboxFlow.first()
            Assert.assertEquals(viewModelCurrentState.hasError, successState.hasError)
            Assert.assertEquals(viewModelCurrentState.canLoadMore, successState.canLoadMore)
            Assert.assertEquals(viewModelCurrentState.isLoading, successState.isLoading)
            Assert.assertEquals(viewModelCurrentState.errorMessage, successState.errorMessage)
            Assert.assertEquals(viewModelCurrentState.data, successState.data)

            verify(mockDispatcher, times(1)).io
            verify(mockRepository, times(1)).getInbox(0)
        }
    }

    @Test
    fun `loadMore should work as expected`() {
        runTest {
            whenever(mockDispatcher.io).thenReturn(Dispatchers.Unconfined)
            whenever(mockRepository.getInbox(1)).thenReturn(mockInboxes)

            val viewModel = InboxViewModel(
                inboxRepository = mockRepository,
                coroutineDispatcherProvider = mockDispatcher
            )

            viewModel.loadMore(1)
            val extraInboxes = listOf(
                mockInbox.copy(
                    userId = 300,
                    userName = "Phil",
                    lastMessageDate = mockInbox.lastMessageDate.minus(500)
                ),
                Inbox(
                    userId = 23,
                    userName = "Sarah",
                    lastMessageDate = 1633111725,
                    userImageBackground = Color.Cyan
                )
            )

            val combinedInboxes = mockInboxes + extraInboxes
            whenever(mockRepository.getInbox(2)).thenReturn(extraInboxes)

            viewModel.loadMore(2)

            Assert.assertEquals(viewModel.currentInboxesDisplayed.size, combinedInboxes.size)
            Assert.assertTrue(viewModel.currentInboxesDisplayed.contains(extraInboxes.first()))
            Assert.assertTrue(viewModel.currentInboxesDisplayed.contains(extraInboxes[1]))

            verify(mockDispatcher, times(2)).io
            verify(mockRepository, times(1)).getInbox(1)
            verify(mockRepository, times(1)).getInbox(2)
        }
    }

    @Test
    fun `viewModel state should handle error properly`() {
        val errorState = InboxViewState(
            data = emptyList(),
            canLoadMore = false,
            hasLoadedMore = false,
            isLoading = false,
            hasError = true,
            hasUpdatedContent = false,
            errorMessage = "fake error"
        )

        runTest {
            whenever(mockDispatcher.io).thenReturn(Dispatchers.Unconfined)
            whenever(mockRepository.getInbox(2)).thenAnswer { Exception() }

            val viewModel = InboxViewModel(
                inboxRepository = mockRepository,
                coroutineDispatcherProvider = mockDispatcher
            )

            viewModel.loadMore(2)

            val viewState = viewModel.inboxFlow.first()
            Assert.assertEquals(viewState.hasError, errorState.hasError)
            Assert.assertTrue(viewState.data.isEmpty())
            verify(mockDispatcher, times(1)).io
            verify(mockRepository, times(1)).getInbox(2)
        }
    }
}

@ExperimentalCoroutinesApi
class MainDispatcherRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()): TestWatcher() {

    override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

    override fun finished(description: Description?) = Dispatchers.resetMain()
}