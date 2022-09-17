package com.hornet.hornetinbox.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hornet.hornetinbox.ui.theme.HornetInboxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: InboxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val inboxViewState = viewModel.inboxFlow.collectAsState(viewModel.initialState).value
            HornetInboxTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize().background(color = Color.White)
                ) {
                    InboxView(
                        state = inboxViewState,
                        handleNewInboxButtonClicked = { viewModel.updateLastMessageTime() },
                        onLoadMoreClicked = { viewModel.loadMore() }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initialise()
    }
}