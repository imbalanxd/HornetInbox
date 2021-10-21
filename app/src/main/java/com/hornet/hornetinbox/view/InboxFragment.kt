package com.hornet.hornetinbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.hornet.hornetinbox.databinding.FragmentInboxBinding
import com.hornet.hornetinbox.view.adapter.InboxPagerAdapter
import com.hornet.hornetinbox.viewmodel.InboxViewModel

class InboxFragment : Fragment() {
    private var binding: FragmentInboxBinding? = null
    private lateinit var inboxPagerAdapter: InboxPagerAdapter
    private val viewModel by activityViewModels<InboxViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
    }

    private fun setupListeners() {
        binding?.inboxFAB?.setOnClickListener {
            viewModel.updateRandomUserTime()
        }
        binding?.inboxViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                val isInboxListUpdated = viewModel.isListUpdate().value
                if (isInboxListUpdated == true) {
                    updatePageSelected()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding?.refreshInboxButton?.setOnClickListener {
            viewModel.clearAllMailPagesResult()
            viewModel.getAllMailPages()
        }
    }

    private fun updatePageSelected() {
        viewModel.isInboxListUpdated(true)
    }

    private fun setupUI() {
        setupViewPager()
    }

    private fun setupViewPager() {
        inboxPagerAdapter = InboxPagerAdapter(childFragmentManager)
        binding?.inboxViewPager?.adapter = inboxPagerAdapter
        binding?.inboxTabLayout?.setupWithViewPager(binding?.inboxViewPager)
    }
}
