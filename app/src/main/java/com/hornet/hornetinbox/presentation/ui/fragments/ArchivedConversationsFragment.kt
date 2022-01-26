package com.hornet.hornetinbox.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hornet.hornetinbox.R
import com.hornet.hornetinbox.databinding.FragmentArchivedConversationsBinding
import com.hornet.hornetinbox.databinding.FragmentMostRecentConversationsBinding
import com.hornet.hornetinbox.presentation.ui.adapters.ConversationsListAdapter
import com.hornet.hornetinbox.presentation.ui.adapters.OnLoadMoreListener
import com.hornet.hornetinbox.presentation.ui.adapters.RecyclerViewLoadMoreScroll
import com.hornet.hornetinbox.presentation.viewmodels.ConversationsViewModel

class ArchivedConversationsFragment : Fragment(R.layout.fragment_archived_conversations) {

    private val model: ConversationsViewModel by activityViewModels()

    private var _binding: FragmentArchivedConversationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var archivedConversationsAdapter: ConversationsListAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArchivedConversationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConversationList()
        observeDisableLoadMore()
        observeArchivedConversations()
        model.fetchArchivedConversations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initConversationList() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext())
        binding.rvArchivedConversations.layoutManager = layoutManager
        archivedConversationsAdapter = ConversationsListAdapter(model.archivedConversations.value!!)
        binding.rvArchivedConversations.adapter = archivedConversationsAdapter

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager as LinearLayoutManager)

        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                model.fetchArchivedConversations()
            }
        })

        binding.rvArchivedConversations.addOnScrollListener(scrollListener)
    }



    private fun observeArchivedConversations() {
        model.archivedConversations.observe(
            this,
            {
                archivedConversationsAdapter.notifyDataSetChanged()
                scrollListener.setLoaded()

            })
    }

    private fun observeDisableLoadMore() {
        model.disableArchivedLoadMore.observe(
            this,
            { disabled ->
                if (disabled)
                    scrollListener.disableLoadMore()
            })
    }
}