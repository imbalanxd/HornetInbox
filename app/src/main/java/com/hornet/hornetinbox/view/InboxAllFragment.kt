package com.hornet.hornetinbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hornet.hornetinbox.databinding.FragmentAllInboxBinding
import com.hornet.hornetinbox.repo.model.MemberDetailsModel
import com.hornet.hornetinbox.view.adapter.InboxAdapter
import com.hornet.hornetinbox.viewmodel.InboxViewModel

class InboxAllFragment: Fragment() {
    private var binding: FragmentAllInboxBinding? = null
    private val viewModel by activityViewModels<InboxViewModel>()
    private var inboxListAdapter: InboxAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllInboxBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.isListUpdate().observe(viewLifecycleOwner) { isUpdated ->
            if(isUpdated){
                updateInboxList()
            }
        }
    }

    private fun updateInboxList(){
        val allInboxList = viewModel.getAllInboxList()
        val sortedInboxList = viewModel.sortMembersList(allInboxList)
        val inboxList = sortedInboxList.subList(fromIndex = 20, sortedInboxList.size)
        setupRecycleView(inboxList)
    }

    private fun setupRecycleView(membersDetailsList: List<MemberDetailsModel>) {
        if(inboxListAdapter == null) {
            inboxListAdapter = InboxAdapter(membersDetailsList)
            val layoutManager = LinearLayoutManager(context)
            binding?.inboxRecyclerView?.adapter = inboxListAdapter
            binding?.inboxRecyclerView?.layoutManager = layoutManager
        } else {
            inboxListAdapter?.updateList(membersDetailsList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}