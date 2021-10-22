package com.hornet.hornetinbox.repo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InboxPageModel (
    var page: Int = 0,
    var members: List<MemberDetailsModel>? = null
): Parcelable