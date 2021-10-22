package com.hornet.hornetinbox.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hornet.hornetinbox.view.InboxAllFragment
import com.hornet.hornetinbox.view.InboxRecentFragment

class InboxPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 2

    override fun getItem(itemPosition: Int): Fragment {
        return when (itemPosition) {
            0 -> {
                InboxRecentFragment()
            }
            else -> {
                InboxAllFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> {
                "Recent (20)"
            }
            1 -> {
                "All Other"
            }
            else -> {
                ""
            }
        }
    }
}