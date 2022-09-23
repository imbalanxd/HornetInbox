package com.hornet.hornetinbox.data

import com.hornet.hornetinbox.data.models.Member

class FakeDataFetcher: DataFetcher {
    companion object {
        val fakeMembers = listOf(
            Member(id = 31, name = "Michael", lastMessage = "1632670025"),
            Member(id = 1, name = "Dave", lastMessage = "1633635725"),
            Member(id = 21, name = "Henry", lastMessage = "1632794725"),
            Member(id = 300, name = "Fredd", lastMessage = "1633121725")
        )
    }
    override suspend fun getPage(page: Int): List<Member> {
        return fakeMembers
    }

}
