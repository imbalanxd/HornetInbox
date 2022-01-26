package com.hornet.hornetinbox

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream
import java.lang.ref.WeakReference


object DataFetcher {
    private var context: WeakReference<Context?>? = null

    fun provideContext(context: Context) {
        this.context = WeakReference(context)
    }

    /**
     * Request a page of data
     */
    fun getPage(pageNum: Int): String {
        val data: InputStream = context?.get()!!.resources.openRawResource(R.raw.data)
        val reader = BufferedReader(InputStreamReader(data))

        val completeSource = reader.readLines()

        return if (completeSource.size > pageNum)
            completeSource[pageNum]
        else
            ""
    }
}