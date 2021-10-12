package com.hornet.hornetinbox

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream


object DataFetcher {
    private var context: Context? = null

    fun provideContext(context: Context) {
        this.context = context
    }

    /**
     * Request a page of data
     */
    fun getPage(pageNum: Int): String {
        val data: InputStream = context!!.resources.openRawResource(R.raw.data)
        val reader = BufferedReader(InputStreamReader(data))

        return reader.readLine()
    }
}