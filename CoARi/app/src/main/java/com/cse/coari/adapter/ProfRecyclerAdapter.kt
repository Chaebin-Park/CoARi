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
import com.cse.coari.data.ProfData
import kotlinx.android.synthetic.main.custom_listview.view.*

class ProfRecyclerAdapter(private val context: Context, private val items: ArrayList<ProfData>) :
    RecyclerView.Adapter<ProfRecyclerAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val profImage = itemView.findViewById<ImageView>(R.id.prof_image)
        private val profName = itemView.findViewById<TextView>(R.id.prof_name)
        private val profEmail = itemView.findViewById<TextView>(R.id.prof_email)
        private val profPhoneNumber = itemView.findViewById<TextView>(R.id.prof_phonenum)
        private val profLab = itemView.findViewById<TextView>(R.id.prof_lab)

        fun bind(profData: ProfData, context: Context){
            if(profData.imgProfile != ""){
                val resourceId =
                    context.resources.getIdentifier(profData.imgProfile, "drawable", context.packageName)
                if(resourceId > 0){
                    profImage.setImageResource(resourceId)
                } else {
                    profImage.setImageResource(R.drawable.ic_deu_logo)
                }
            } else {
                profImage.setImageResource(R.drawable.ic_deu_logo)
            }

            profName.text = profData.strName
            profEmail.text = profData.strEmail
            profPhoneNumber.text = profData.strPhoneNumber
            profLab.text = profData.strLab
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_prof_info, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    override fun getItemCount(): Int = items.size
}