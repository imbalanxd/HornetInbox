package com.hornet.hornetinbox.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hornet.hornetinbox.R
import com.hornet.hornetinbox.common.util.date.DateUtil
import com.hornet.hornetinbox.repo.model.MemberDetailsModel

class InboxAdapter(private var memberList: List<MemberDetailsModel>) :
    RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val initialsTextView: TextView = view.findViewById(R.id.initialsTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.inbox_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nameTextView.text = memberList[position].name
        viewHolder.initialsTextView.text = memberList[position].profileInitial
        viewHolder.dateTextView.text =
            DateUtil.convertLongToTime(memberList[position].last_message_at)
    }

    fun updateList(memberList: List<MemberDetailsModel>) {
        this.memberList = memberList
        notifyDataSetChanged()
    }

    override fun getItemCount() = memberList.size
}