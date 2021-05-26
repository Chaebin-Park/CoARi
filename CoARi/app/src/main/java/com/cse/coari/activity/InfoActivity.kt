package com.cse.coari.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cse.coari.R
import com.cse.coari.adapter.InfoFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    
    private val tabTextList = arrayListOf("교육목표 및 연혁", "위치 및 연락처")
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        init()
    }

    private fun init(){
        info_viewpager.adapter = InfoFragmentStateAdapter(this)
        TabLayoutMediator(info_tab, info_viewpager){ tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }
}