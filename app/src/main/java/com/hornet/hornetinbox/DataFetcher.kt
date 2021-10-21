package com.hornet.hornetinbox

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.InputStream
import java.lang.ref.WeakReference

object DataFetcher {
    private val TAG = DataFetcher::class.java.simpleName
    private var context: WeakReference<Context?>? = null

    fun provideContext(context: Context) {
        this.context = WeakReference(context)
    }

    @Deprecated("pageNum num not considered", ReplaceWith("com.hornet.hornetinbox.viewmodel.InboxViewModel.getInboxPage(pageNum: Int))"))
    fun getPage(pageNum: Int): String {
        context?.get()?.let {
            try {
                val data: InputStream = it.resources.openRawResource(R.raw.data)
                val reader = BufferedReader(InputStreamReader(data))

                return reader.readLine()
            } catch (exception: IOException) {
                Log.d(TAG, exception.message ?: "Could not read a line of text.")
            }
        }

        return ""
    }

    fun getAllPages(): List<String> {
        val pages = arrayListOf<String>()

        context?.get()?.let {
            try {
                val data: InputStream = it.resources.openRawResource(R.raw.data)
                val reader = BufferedReader(InputStreamReader(data))
                var line = reader.readLine()

                while (line != null) {
                    pages.add(line)
                    line = reader.readLine()
                }
            } catch (exception: IOException) {
                Log.d(TAG, exception.message ?: "Could not read a line of text.")
            }
        }

        return pages
    }
}