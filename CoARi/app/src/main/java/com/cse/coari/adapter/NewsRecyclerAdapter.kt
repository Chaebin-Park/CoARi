package com.cse.coari.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cse.coari.R
import com.cse.coari.activity.DetailEmpActivity
import com.cse.coari.activity.DetailNewsActivity
import com.cse.coari.data.NewsData

class NewsRecyclerAdapter(private val context: Context, private val items: ArrayList<NewsData>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val newsImage = itemView.findViewById<ImageView>(R.id.news_image)
        private val newsTitle = itemView.findViewById<TextView>(R.id.news_title)
        private val newsDate = itemView.findViewById<TextView>(R.id.news_date)

        fun bind(newsData: NewsData, context: Context){
            if(newsData.imgProfile != ""){
                val resourceId =
                    context.resources.getIdentifier(newsData.imgProfile, "drawable", context.packageName)
                if(resourceId > 0){
                    newsImage.setImageResource(resourceId)
                } else {
                    newsImage.setImageResource(R.drawable.ic_deu_logo)
                }
            } else {
                newsImage.setImageResource(R.drawable.ic_deu_logo)
            }

            newsTitle.text = newsData.strTitle
            newsDate.text = newsData.strDate

            itemView.setOnClickListener{
                Intent(context, DetailNewsActivity::class.java).apply {
                    putExtra("data", newsData)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    override fun getItemCount(): Int = items.size
}