package com.cse.coari.activity

import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cse.coari.R
import com.cse.coari.data.ProfData
import kotlinx.android.synthetic.main.activity_detail_prof_info.*

class DetailProfInfoActivity : AppCompatActivity() {

    private lateinit var profData: ProfData
    private lateinit var btnMoreInfo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detail_prof_info)

        profData = intent.getSerializableExtra("data") as ProfData
        Glide.with(this).load(resources.getIdentifier(profData.imgProfile, "drawable", packageName)).into(detail_prof_photo)
        detail_prof_name.text = profData.strName
        detail_prof_email.text = profData.strEmail
        detail_prof_phonenum.text = profData.strPhoneNumber
        detail_prof_research.text = profData.strResearch


    }
}