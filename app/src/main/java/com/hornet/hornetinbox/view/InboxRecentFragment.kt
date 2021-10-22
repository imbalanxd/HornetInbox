package com.hornet.hornetinbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hornet.hornetinbox.databinding.FragmentRecentInboxBinding
import com.hornet.hornetinbox.repo.model.InboxPageModel
import com.hornet.hornetinbox.repo.model.MemberDetailsModel
import com.hornet.hornetinbox.view.adapter.InboxAdapter
import com.hornet.hornetinbox.viewmodel.InboxViewModel
import android.view.animation.LayoutAnimationController

import android.R
import android.view.animation.AnimationUtils


class InboxRecentFragment : Fragment() {
    private var binding: FragmentRecentInboxBinding? = null
    private val viewModel by activityViewModels<InboxViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentInboxBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        viewModel.getAllMailPages()
    }

    private fun setupObservers() {
        viewModel.getAllMailPagesResults().observe(viewLifecycleOwner) { pageList ->
            pageList?.let {
                showInitialPage(pageList)
            }
        }

        viewModel.isListUpdate().observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                updateInboxList()
            }
        }
    }

    private fun showInitialPage(pageList: List<InboxPageModel?>) {
/*        if (pageList.isNotEmpty()) {//TODO: Includes pagination.
            viewModel.setRecentPageAdded(0)
            pageList[0]?.members?.let { members ->
                viewModel.addInboxMembersToRecentInboxList(members)
                val sortedMembersList = viewModel.sortMembersList(members)
                setupRecycleView(sortedMembersList)
            }
        }*/
        if (pageList.isNotEmpty()) {//TODO: Pagination not included, should be updated.
            viewModel.setAllInboxMemberList(pageList)
            updateInboxList()
        }
    }

    private fun updateInboxList() {
        val allInboxList = viewModel.getAllInboxList()
        val sortedInboxList = viewModel.sortMembersList(allInboxList)
        val inboxList = sortedInboxList.subList(0, 20)
        setupRecycleView(inboxList)
    }

    private fun setupRecycleView(membersDetailsList: List<MemberDetailsModel>) {
        if (inboxListAdapter == null) {
            inboxListAdapter = InboxAdapter(membersDetailsList)
            val linearLayoutManager = LinearLayoutManager(context)
            binding?.inboxRecyclerView?.apply {
                adapter = inboxListAdapter
                layoutManager = linearLayoutManager
                //layoutAnimation = getScaleAnimation()
            }
            //setOnScrollListener(binding?.inboxRecyclerView, layoutManager)TODO: Includes pagination.
        } else {
            inboxListAdapter?.updateList(membersDetailsList)
        }
    }

    private fun getScaleAnimation(): LayoutAnimationController{
        return AnimationUtils.loadLayoutAnimation(context, R.anim.slide_in_left)
    }

    /*TODO(ask about pagination confusion with most-recent list): WIP Start of Pagination section*/
    private var inboxListAdapter: InboxAdapter? = null
    private var pastVisiblesItems = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private fun setOnScrollListener(
        recyclerView: RecyclerView?,
        mLayoutManager: LinearLayoutManager
    ) {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        val nextPageNumberToAdd = viewModel.getRecentPageAdded() + 1
                        if (viewModel.getRecentInboxList().size < 20) {
                            val nextPage = viewModel.getInboxPage(nextPageNumberToAdd)
                            nextPage?.members?.let { members ->
                                viewModel.setRecentPageAdded(nextPageNumberToAdd)
                                viewModel.addInboxMembersToRecentInboxList(members)
                            }
                            val updatedList =
                                viewModel.getRecentInboxList() as ArrayList<MemberDetailsModel>
                            val sortedUpdatedList = viewModel.sortMembersList(updatedList)
                            inboxListAdapter?.updateList(sortedUpdatedList)
                        }
                    }
                }
            }
        })
    }

    /*TODO(ask about pagination): End of Pagination section*/

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}