package com.hornet.hornetinbox.presentation.viewModel

import com.hornet.hornetinbox.CoroutineDispatcherProvider
import com.hornet.hornetinbox.data.FakeDataFetcher
import com.hornet.hornetinbox.data.InboxResult
import com.hornet.hornetinbox.data.mapper.MemberToInboxMapper
import com.hornet.hornetinbox.data.models.Member
import com.hornet.hornetinbox.domain.repository.InboxRepository
import com.hornet.hornetinbox.domain.usecases.GetInboxUseCase
import com.hornet.hornetinbox.domain.usecases.UpdateInboxContentUseCase
import com.hornet.hornetinbox.domain.usecases.UpdateRandomInboxTimeUseCase
import com.hornet.hornetinbox.presentation.ui.InboxViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HornetInboxViewModelUnitTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockDispatcher: CoroutineDispatcherProvider = mock()
    private val mockGetInboxUseCase: GetInboxUseCase = mock()
    private val mockUpdateInboxContentUseCase: UpdateInboxContentUseCase = mock()
    private val mockUpdateRandomInboxTimeUseCase: UpdateRandomInboxTimeUseCase = mock()

    @Test
    fun `viewModel should set initial state correctly`() {
        val viewModel = InboxViewModel(
            getInboxUseCase = mockGetInboxUseCase,
            updateInboxContentUseCase = mockUpdateInboxContentUseCase,
            updateRandomInboxTimeUseCase = mockUpdateRandomInboxTimeUseCase
        )

        val inboxState = viewModel.inboxState.value

        Assert.assertTrue(viewModel.nextPageToLoad == 0)
        Assert.assertTrue(viewModel.currentInboxesDisplayed.isEmpty())
        Assert.assertFalse(inboxState.hasError)
        Assert.assertFalse(inboxState.hasLoadedMore)
        Assert.assertFalse(inboxState.canLoadMore)
        Assert.assertTrue(inboxState.data.isEmpty())
        Assert.assertNull(inboxState.errorMessage)
    }

    @Test
    fun `initialise method should fetch first page of data and set correct state`() {
        runTest {
            val mockMembers = FakeDataFetcher.fakeMembers
            val mockInbox = mockMembers.map { MemberToInboxMapper().map(it) }
            val mockCanLoadMore = true
            whenever(mockGetInboxUseCase(0, isLoadingMore = false))
                .thenReturn(flowOf(InboxResult.Success(data = mockInbox, mockCanLoadMore)))
            whenever(mockUpdateInboxContentUseCase(emptyList(), mockInbox)).thenReturn(mockInbox)

            val viewModel = InboxViewModel(
                getInboxUseCase = mockGetInboxUseCase,
                updateInboxContentUseCase = mockUpdateInboxContentUseCase,
                updateRandomInboxTimeUseCase = mockUpdateRandomInboxTimeUseCase
            )

            viewModel.initialise()

            val viewModelCurrentState = viewModel.inboxState.value
            Assert.assertFalse(viewModelCurrentState.hasError)
            Assert.assertNull(viewModelCurrentState.errorMessage)
            Assert.assertTrue(mockInbox.size == mockMembers.size)
            Assert.assertTrue(mockInbox.first().userId == mockMembers.first().id)
        }
    }

    @Test
    fun `loadMore should fetch inbox and update list correctly`() {
        runTest {
            val mockMembers = FakeDataFetcher.fakeMembers.subList(0, 2)
            val mockInbox = mockMembers.map { MemberToInboxMapper().map(it) }
            val mockMembers2 = FakeDataFetcher.fakeMembers.subList(2, 4)
            val mockInbox2 = mockMembers2.map { MemberToInboxMapper().map(it) }
            val mockCanLoadMore = true

            whenever(mockDispatcher.io).thenReturn(Dispatchers.Unconfined)

            whenever(mockGetInboxUseCase(0, isLoadingMore = false))
                .thenReturn(
                    flowOf(InboxResult.Success(data = mockInbox, mockCanLoadMore))
                )
            whenever(mockGetInboxUseCase(1, isLoadingMore = true))
                .thenReturn(flowOf(InboxResult.Success(mockInbox2, true)))

            whenever(mockUpdateInboxContentUseCase(emptyList(), mockInbox)).thenReturn(mockInbox)
            whenever(
                mockUpdateInboxContentUseCase(
                    mockInbox,
                    mockInbox2
                )
            ).thenReturn(mockInbox + mockInbox2)


            val viewModel = InboxViewModel(
                getInboxUseCase = mockGetInboxUseCase,
                updateInboxContentUseCase = mockUpdateInboxContentUseCase,
                updateRandomInboxTimeUseCase = mockUpdateRandomInboxTimeUseCase
            )

            Assert.assertTrue(viewModel.currentInboxesDisplayed.isEmpty())
            Assert.assertTrue(viewModel.nextPageToLoad == 0)

            viewModel.initialise()
            delay(500)
            var viewModelCurrentState = viewModel.inboxState.value
            Assert.assertFalse(viewModelCurrentState.hasError)
            Assert.assertNull(viewModelCurrentState.errorMessage)
            Assert.assertTrue(mockInbox.size == viewModel.currentInboxesDisplayed.size)
            Assert.assertTrue(mockInbox.containsAll(viewModel.currentInboxesDisplayed))

            verify(mockGetInboxUseCase, times(1)).invoke(0, false)
            verify(mockUpdateInboxContentUseCase, times(1)).invoke(emptyList(), mockInbox)

            viewModel.loadMore(1)

            delay(500)
            viewModelCurrentState = viewModel.inboxState.value
            Assert.assertFalse(viewModelCurrentState.hasError)
            Assert.assertNull(viewModelCurrentState.errorMessage)
            Assert.assertTrue(viewModel.currentInboxesDisplayed.size == FakeDataFetcher.fakeMembers.size)
            Assert.assertTrue(viewModel.currentInboxesDisplayed.containsAll(mockInbox))
            Assert.assertTrue(viewModel.currentInboxesDisplayed.containsAll(mockInbox2))

            verify(mockGetInboxUseCase, times(1)).invoke(1, true)
            verify(mockUpdateInboxContentUseCase, times(1)).invoke(mockInbox, mockInbox2)
        }
    }

    @Test
    fun `updateLastMessageTime should work appropriately`() {
        runTest {
            val mockMembers = FakeDataFetcher.fakeMembers
            val mockInbox = mockMembers.map { MemberToInboxMapper().map(it) }

            whenever(mockDispatcher.default).thenReturn(Dispatchers.Unconfined)
            whenever(mockGetInboxUseCase.invoke(0, false)).thenReturn(
                flowOf(InboxResult.Success(mockInbox, true))
            )

            val updateRandomInboxTimeUseCase = UpdateRandomInboxTimeUseCase(
                coroutineDispatcherProvider = mockDispatcher,
                updateInboxContentUseCase = UpdateInboxContentUseCase(mockDispatcher)
            )

            val viewModel = InboxViewModel(
                getInboxUseCase = mockGetInboxUseCase,
                updateInboxContentUseCase = UpdateInboxContentUseCase(mockDispatcher),
                updateRandomInboxTimeUseCase = updateRandomInboxTimeUseCase
            )

            val mockTime = System.currentTimeMillis()

            viewModel.initialise()
            delay(500)

            viewModel.updateLastMessageTime(mockTime)

            delay(200)

            val state = viewModel.inboxState.value
            Assert.assertTrue(state.data.isNotEmpty())
            Assert.assertTrue(state.data.first().lastMessageDate == mockTime)
            Assert.assertTrue(state.data.size == mockInbox.size)
        }
    }

    @Test
    fun `viewModel state should handle error properly`() {
        runTest {
            val fakeRepo = object : InboxRepository {
                override suspend fun getInbox(page: Int): List<Member> {
                    throw IllegalArgumentException()
                }
            }

            val getInboxUseCase = GetInboxUseCase(
                repository = fakeRepo,
                mapper = MemberToInboxMapper()
            )

            val viewModel = InboxViewModel(
                getInboxUseCase = getInboxUseCase,
                updateInboxContentUseCase = mockUpdateInboxContentUseCase,
                updateRandomInboxTimeUseCase = mockUpdateRandomInboxTimeUseCase
            )

            viewModel.initialise()

            delay(500)

            val state = viewModel.inboxState.value

            Assert.assertTrue(state.hasError)
            Assert.assertNotNull(state.errorMessage)
            Assert.assertEquals("Yikes :(", state.errorMessage)
        }
    }
}


@ExperimentalCoroutinesApi
class MainDispatcherRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()): TestWatcher() {

    override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

    override fun finished(description: Description?) = Dispatchers.resetMain()
}