package com.hornet.hornetinbox.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hornet.hornetinbox.R

val HornetInboxFont = FontFamily(
    Font(R.font.ubuntu_regular, FontWeight.Normal),
    Font(R.font.ubuntu_bold, FontWeight.Bold)
)
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = HornetInboxFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    body2 = TextStyle(
        fontFamily = HornetInboxFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)