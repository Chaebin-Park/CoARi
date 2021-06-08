package com.cse.coari.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cse.coari.R
import com.cse.coari.data.AlarmDTO
import com.cse.coari.data.AlarmDTOItem
import com.cse.coari.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipeListAdapter(private val context: Context, private var items: AlarmDTO)
    : RecyclerView.Adapter<SwipeListAdapter.ItemViewHolder>() {

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//    inner class ItemViewHolder(private val binding: ItemAlarmBinding)
//    : RecyclerView.ViewHolder(binding.root){
    inner class ItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){

        private val deleteImg: ImageView = itemView.findViewById(R.id.iv_delete_alarm)
        private val alarmTitle = itemView.findViewById<TextView>(R.id.tv_alarm_title)
        private val alarmAuthor = itemView.findViewById<TextView>(R.id.tv_alarm_author)
        private val alarmDate = itemView.findViewById<TextView>(R.id.tv_alarm_date)

        fun bind(data: AlarmDTOItem, context: Context){
            val functionName = object {}.javaClass.enclosingMethod.name

            Glide.with(itemView).load(R.raw.ic_delete_arrow).into(deleteImg)

            alarmTitle.text = data.title
            alarmAuthor.text = data.author
            alarmDate.text = data.sendTime
            deleteImg.setOnClickListener{
                val id = RetrofitBuilder.api.deleteAlarm(data.alarmId.toString())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false)
        return ItemViewHolder(view)
    }
//    = ItemViewHolder(
//        ItemAlarmBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: AlarmDTO){
        this.items = data
    }

    fun removeItem(position : Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    companion object{
        const val TAG = "SwipeListAdapter"
        const val INNER_TAB = "SwipeListAdapter.ItemViewHolder"
    }
}