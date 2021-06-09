package com.cse.coari.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.cse.coari.R
import com.cse.coari.adapter.HofRecyclerAdapter
import com.cse.coari.data.GetHofDTO
import com.cse.coari.data.PostHofDTO
import com.cse.coari.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_detail_emp.*
import kotlinx.android.synthetic.main.activity_emp.*
import kotlinx.android.synthetic.main.item_hof.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEmpActivity : Activity() {

    private lateinit var hofItem : PostHofDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detail_emp)

        val id = intent.getStringExtra("ID") as String

        RetrofitBuilder.api.postHof(id).enqueue(object : Callback<PostHofDTO>{
            override fun onResponse(call: Call<PostHofDTO>, response: Response<PostHofDTO>) {
                hofItem = response.body()!!
                Glide.with(this@DetailEmpActivity).load(hofItem.fileURL).into(iv_profile)
                tv_name.text = hofItem.name
                tv_company.text = hofItem.company
                tv_company_info.text = hofItem.companyInfo
                tv_work.text = hofItem.work
            }

            override fun onFailure(call: Call<PostHofDTO>, t: Throwable) {
                Log.i("DetailEmpActivity", "Get hof data fail")
            }

        })
    }
}