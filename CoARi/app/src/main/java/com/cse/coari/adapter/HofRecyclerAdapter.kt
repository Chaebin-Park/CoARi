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
import com.cse.coari.activity.DetailProfInfoActivity
import com.cse.coari.data.HofData

class HofRecyclerAdapter(private val context: Context, private val items: ArrayList<HofData>) :
    RecyclerView.Adapter<HofRecyclerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val hofImage = itemView.findViewById<ImageView>(R.id.hof_image)
        private val hofName = itemView.findViewById<TextView>(R.id.hof_name)

        fun bind(hofData: HofData, context: Context){
            if(hofData.imgProfile != ""){
                val resourceId =
                    context.resources.getIdentifier(hofData.imgProfile, "drawable", context.packageName)
                if(resourceId > 0){
                    hofImage.setImageResource(resourceId)
                } else {
                    hofImage.setImageResource(R.drawable.ic_deu_logo)
                }
            } else {
                hofImage.setImageResource(R.drawable.ic_deu_logo)
            }

            hofName.text = hofData.strName

            itemView.setOnClickListener{
                Intent(context, DetailEmpActivity::class.java).apply {
                    putExtra("data", hofData)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_hof, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    override fun getItemCount(): Int = items.size
}