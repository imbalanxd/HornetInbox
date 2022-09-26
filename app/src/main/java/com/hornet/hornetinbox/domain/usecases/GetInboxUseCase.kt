package com.hornet.hornetinbox.domain.usecases

import android.content.res.Resources.NotFoundException
import com.google.gson.JsonParseException
import com.hornet.hornetinbox.data.InboxResult
import com.hornet.hornetinbox.data.mapper.MemberToInboxMapper
import com.hornet.hornetinbox.data.models.Inbox
import com.hornet.hornetinbox.domain.repository.InboxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetInboxUseCase @Inject constructor(
    private val repository: InboxRepository,
    private val mapper: MemberToInboxMapper
) {
    suspend operator fun invoke(
        page: Int,
        isLoadingMore: Boolean = false
    ): Flow<InboxResult<List<Inbox>>> {
        return flow {
            if (!isLoadingMore) emit(InboxResult.Loading)
            try {
                val inbox = repository.getInbox(page).map { member -> mapper.map(member) }
                emit(InboxResult.Success(inbox, page < PAGE_LIMIT))
            } catch (illegalArgumentException: IllegalArgumentException) {
                emit(InboxResult.Error("Yikes :("))
            } catch (ioException: IOException) {
                emit(InboxResult.Error("What have we done ?!"))
            } catch (ignoredException: NotFoundException) {
                emit(InboxResult.Error("Oh damn, we messed up :("))
            } catch (jsonException: JsonParseException) {
                emit(InboxResult.Error("It's not you, it's us :("))
            }
        }
    }

    private companion object {
        const val PAGE_LIMIT = 4
    }
}