package com.hornet.hornetinbox.data

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson
import com.hornet.hornetinbox.R
import com.hornet.hornetinbox.data.models.Member
import com.hornet.hornetinbox.data.models.MemberResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream
import javax.inject.Inject


class DataFetcherImpl @Inject constructor(@ApplicationContext val context: Context): DataFetcher {

    private val rawMemberResponse: List<String>

    init {
        rawMemberResponse = readFromFile(context = context)
    }

    /**
     * Request a page of data
     */
    override suspend fun getPage(page: Int): List<Member> {
        val allMembers = arrayListOf<MemberResponse>()
        rawMemberResponse.map {
            val memberResponse = parseReadLineToMemberResponse(it)
            if (memberResponse != null) allMembers.add(memberResponse)
        }
        return allMembers
            .firstOrNull { membersResponse -> page == membersResponse.page }
            ?.members
            ?: emptyList()
    }

    private fun readFromFile(
        context: Context,
        @RawRes resourceId: Int = R.raw.data
    ): List<String> {
        val data: InputStream = context.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(data))
        return reader.readLines()
    }

    private fun parseReadLineToMemberResponse(rawMemberResponse: String): MemberResponse? {
        return Gson().fromJson(rawMemberResponse, MemberResponse::class.java)
    }
}