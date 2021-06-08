package com.cse.coari.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cse.coari.R
import kotlinx.android.synthetic.main.activity_alarm_main.*

class AlarmMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_main)

        val vpAdapter = ViewPagerAdapter(supportFragmentManager)
        viewpager.adapter = vpAdapter
    }
}