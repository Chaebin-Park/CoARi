package com.cse.coari.alarm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cse.coari.R
import com.cse.coari.data.AlarmDTOItem
import com.cse.coari.databinding.ItemSwipeBinding
import kotlinx.android.synthetic.main.activity_alarm.view.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AlarmAdapter(private val viewModel: AlarmViewModel) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemSwipeBinding) : RecyclerView.ViewHolder(binding.root){
        private lateinit var data : AlarmDTOItem

        fun bind(viewModel: AlarmViewModel, pos: Int){
            data = viewModel.getAlarmDTO()[pos]

            binding.title = data.title
            binding.author = data.author
            binding.date = data.sendTime
            Glide.with(binding.root).load(R.raw.ic_delete_arrow).into(binding.deleteAlarm)
            binding.deleteAlarm.setOnClickListener{
                Log.e("__ALARM", "AlarmAdapter : delete {${data.alarmId}} alarm")
                viewModel.deleteAlarm(data.alarmId.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSwipeBinding = ItemSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int = viewModel.getAlarmDTO().size
}