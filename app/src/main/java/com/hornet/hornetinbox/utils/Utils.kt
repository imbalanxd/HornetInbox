package com.hornet.hornetinbox.utils

import android.graphics.Color

class Utils {

    companion object {

        val randomColor: Int
            get() {
                return Color.rgb((30..200).random(), (30..200).random(), (30..200).random())
            }
    }
}