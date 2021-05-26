package com.cse.coari.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cse.coari.fragment.EduGoalFragment
import com.cse.coari.fragment.LocationFragment

class InfoFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> LocationFragment()
            else -> EduGoalFragment()
        }
    }
}