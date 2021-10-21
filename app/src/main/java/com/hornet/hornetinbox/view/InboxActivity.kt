package com.hornet.hornetinbox.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hornet.hornetinbox.DataFetcher
import com.hornet.hornetinbox.R

class InboxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        DataFetcher.provideContext(this)
    }
}