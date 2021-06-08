package com.cse.coari.alarm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class ViewPagerAdapter(fm: FragmentManager?): FragmentPagerAdapter(fm!!) {
    private val items: ArrayList<Fragment> = ArrayList()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    init{
        items.add(AlarmFragment())
    }
}