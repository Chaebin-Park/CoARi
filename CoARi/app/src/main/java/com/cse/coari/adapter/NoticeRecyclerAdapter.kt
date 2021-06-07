package com.cse.coari.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cse.coari.R
import com.cse.coari.activity.DetailNoticeActivity
import com.cse.coari.data.NoticeData

class NoticeRecyclerAdapter(private val context: Context, private val items: ArrayList<NoticeData>) :
    RecyclerView.Adapter<NoticeRecyclerAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val noticeTitle = itemView.findViewById<TextView>(R.id.notice_title)
        private val noticeContext = itemView.findViewById<TextView>(R.id.notice_context)
        private val noticeDate = itemView.findViewById<TextView>(R.id.notice_date)

        fun bind(noticeData: NoticeData, context: Context){
            noticeTitle.text = noticeData.strTitle
            noticeContext.text = noticeData.strContext
            noticeDate.text = noticeData.strDate

            itemView.setOnClickListener{
                Intent(context, DetailNoticeActivity::class.java).apply {
                    putExtra("notice_data", noticeData)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    override fun getItemCount(): Int = items.size
}