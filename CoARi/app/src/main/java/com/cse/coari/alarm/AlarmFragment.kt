package com.cse.coari.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cse.coari.R
import com.cse.coari.databinding.FragmentAlarmBinding

@Suppress("DEPRECATION")
class AlarmFragment : Fragment() {
    private lateinit var vm: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentAlarmBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_alarm, container, false)
        init(binding)

        binding.refresh.setOnRefreshListener {
            vm.refresh()
            binding.alarmViewModel = vm
            binding.lifecycleOwner = this
            binding.refresh.isRefreshing = false
        }

        binding.btnRefresh.setOnClickListener{
            vm.refresh()
            binding.alarmViewModel = vm
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    private fun init(binding: FragmentAlarmBinding){
        vm = ViewModelProvider(this).get(AlarmViewModel::class.java)
        vm.viewInit(binding.fragAlarmRecycler)
        vm.getAlarms()
        binding.alarmViewModel = vm
        binding.lifecycleOwner = this
    }
}