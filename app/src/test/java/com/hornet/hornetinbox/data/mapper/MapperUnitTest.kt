package com.hornet.hornetinbox.data.mapper

import com.hornet.hornetinbox.data.models.Member
import org.junit.Assert
import org.junit.Test

class MapperUnitTest {
    private val mockMember = Member(name = "Michael", id = 31, lastMessage = "1632670025")
    private val mockMembers = listOf(
        mockMember,
        Member(name = "Henry", id = 21, lastMessage = "1632794725"),
        Member(name = "Ivan", id = 11, lastMessage = "1633121725")
    )
    @Test
    fun `mapper should map from Member object to Inbox object correctly`() {
        val mapper = MemberToChatMapper()
        val inbox = mapper.map(mockMember)
        Assert.assertTrue(mockMember.id == inbox.userId)
        Assert.assertTrue(mockMember.name == inbox.userName)
        Assert.assertTrue(mockMember.lastMessage == inbox.lastMessageDate.toString())

        val mappedList = mockMembers.map { member -> mapper.map(member) }

        Assert.assertTrue(mockMembers.size == mappedList.size)
        Assert.assertTrue(mappedList.first().userName == mockMembers.first().name)
    }

}