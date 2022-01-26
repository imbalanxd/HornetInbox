package com.hornet.hornetinbox.presentation.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.hornet.hornetinbox.R
import com.hornet.hornetinbox.databinding.ActivityMainBinding
import com.hornet.hornetinbox.presentation.ui.adapters.ConversationsFragmentStateAdapter
import com.hornet.hornetinbox.presentation.viewmodels.ConversationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val model: ConversationsViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initConversationsViewPager()

        binding.fabAddNewChat.setOnClickListener {
            model.updateRandomConversation()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            model.refreshInbox()
        }


        model.refreshing.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = it
        })
    }

    private fun initConversationsViewPager() {
        binding.vpConversations.adapter = ConversationsFragmentStateAdapter(this)
        binding.vpConversations.offscreenPageLimit = 3

        TabLayoutMediator(binding.tabLayout, binding.vpConversations) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.recent)
                }
                else -> {
                    tab.text = getString(R.string.archived)
                }
            }
        }.attach()
    }
}