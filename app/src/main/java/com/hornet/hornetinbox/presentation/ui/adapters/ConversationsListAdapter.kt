package com.hornet.hornetinbox.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hornet.hornetinbox.data.models.dto.Conversation
import com.hornet.hornetinbox.databinding.ItemConversationListBinding

class ConversationsListAdapter(private var memberList: List<Conversation>) :
    RecyclerView.Adapter<ConversationsListAdapter.ConversationViewHolder>() {

    inner class ConversationViewHolder(val binding: ItemConversationListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationsListAdapter.ConversationViewHolder {
        val holderBinding =
            ItemConversationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ConversationViewHolder(holderBinding)
    }

    override fun onBindViewHolder(
        holder: ConversationsListAdapter.ConversationViewHolder,
        position: Int
    ) {
        with(holder) {
            with(memberList[position]) {
                binding.tvName.text = this.name
                binding.tvRelativeTime.text = this.relativeTime
                binding.tvInitials.text = this.initials
                binding.ivProfile.setBackgroundColor(this.initialsBackgroundColor)
            }
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}