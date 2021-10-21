package com.hornet.hornetinbox.repo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MemberDetailsModel(
    var name: String? = null,
    var id: Int = -1,
    var last_message_at: Long = -1,
    var date: Date? = null,
    var profileInitial: String? = null
) : Parcelable