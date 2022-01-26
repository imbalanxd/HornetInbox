package com.hornet.hornetinbox.presentation.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hornet.hornetinbox.presentation.ui.fragments.ArchivedConversationsFragment
import com.hornet.hornetinbox.presentation.ui.fragments.MostRecentConversationsFragment

class ConversationsFragmentStateAdapter(actvity : AppCompatActivity) : FragmentStateAdapter(actvity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            MostRecentConversationsFragment()
        } else {
            ArchivedConversationsFragment()
        }
    }
}