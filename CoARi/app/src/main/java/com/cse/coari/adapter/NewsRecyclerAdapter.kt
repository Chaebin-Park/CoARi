package com.cse.coari.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cse.coari.R
import com.cse.coari.activity.DetailNewsActivity
import com.cse.coari.activity.NewsActivity
import com.cse.coari.data.GetNewsDTOItem

class NewsRecyclerAdapter(private val context: Context, private val itemGets: ArrayList<GetNewsDTOItem>) :
    RecyclerView.Adapter<NewsRecyclerAdapter.ItemViewHolder>(){
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val newsImage = itemView.findViewById<ImageView>(R.id.news_image)
        private val newsTitle = itemView.findViewById<TextView>(R.id.news_title)
        private val newsDate = itemView.findViewById<TextView>(R.id.news_date)

        @SuppressLint("SetTextI18n")
        fun bind(getNewsData: GetNewsDTOItem, context: Context){
            val functionName = object {}.javaClass.enclosingMethod.name
            Log.i(TAG, "$functionName : GetNewsDTOItem : {$getNewsData}")
            if(getNewsData.thumbnail != ""){
                Glide.with(context).load(getNewsData.thumbnail).into(newsImage)
            } else {
                newsImage.setImageResource(R.drawable.ic_deu_logo)
            }

            newsTitle.text = getNewsData.title
            newsDate.text = "Date : ${getNewsData.date}"

            itemView.setOnClickListener{
                Intent(context, DetailNewsActivity::class.java).apply {
                    putExtra("data", getNewsData.url)
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
        holder.bind(itemGets[position], context)
    }

    override fun getItemCount(): Int = itemGets.size

    companion object{
        const val TAG = "NewsRecyclerAdapter"
        const val INNER_TAG = "NewsRecyclerAdapter.ItemViewHolder"
    }
}