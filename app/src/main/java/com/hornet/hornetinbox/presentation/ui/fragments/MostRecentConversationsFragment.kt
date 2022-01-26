package com.hornet.hornetinbox.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hornet.hornetinbox.R
import com.hornet.hornetinbox.databinding.FragmentMostRecentConversationsBinding
import com.hornet.hornetinbox.presentation.ui.adapters.ConversationsListAdapter
import com.hornet.hornetinbox.presentation.ui.adapters.OnLoadMoreListener
import com.hornet.hornetinbox.presentation.ui.adapters.RecyclerViewLoadMoreScroll
import com.hornet.hornetinbox.presentation.viewmodels.ConversationsViewModel

class MostRecentConversationsFragment : Fragment(R.layout.fragment_most_recent_conversations) {

    private val model: ConversationsViewModel by activityViewModels()

    private var _binding: FragmentMostRecentConversationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recentConversationsAdapter: ConversationsListAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMostRecentConversationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConversationList()
        observeDisableLoadMore()
        observeMostRecentConversations()
        model.fetchMostRecentConversations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initConversationList() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext())
        binding.rvMostRecentConversations.layoutManager = layoutManager
        recentConversationsAdapter = ConversationsListAdapter(model.mostRecentConversations.value!!)
        binding.rvMostRecentConversations.adapter = recentConversationsAdapter

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager as LinearLayoutManager)

        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                model.fetchMostRecentConversations()
            }
        })

        binding.rvMostRecentConversations.addOnScrollListener(scrollListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeMostRecentConversations() {
        model.mostRecentConversations.observe(
            this,
            {
                recentConversationsAdapter.notifyDataSetChanged()
                scrollListener.setLoaded()
            })
    }

    private fun observeDisableLoadMore() {
        model.disableMostRecentLoadMore.observe(
            this,
            { disabled ->
                if (disabled)
                    scrollListener.disableLoadMore()
            })
    }


}