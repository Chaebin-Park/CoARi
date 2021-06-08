package com.cse.coari.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cse.coari.R
import com.cse.coari.adapter.NewsRecyclerAdapter
import com.cse.coari.data.GetNewsDTO
import com.cse.coari.data.GetNewsDTOItem
import com.cse.coari.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewsActivity : AppCompatActivity() {

    private val newsList = arrayListOf<GetNewsDTOItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val functionName = object {}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Activity Start")

        var list: GetNewsDTO
        RetrofitBuilder.api.getNews().enqueue(object : Callback<GetNewsDTO>{
            override fun onResponse(call: Call<GetNewsDTO>, response: Response<GetNewsDTO>) {
                if (response.isSuccessful){
                    list = response.body()!!
                    val adapter = NewsRecyclerAdapter(this@NewsActivity, list)
                    news_recycler.adapter = adapter

                    val layout = LinearLayoutManager(this@NewsActivity)
                    news_recycler.layoutManager = layout
                    news_recycler.setHasFixedSize(true)
                }
            }

            override fun onFailure(call: Call<GetNewsDTO>, t: Throwable) {
                Log.i(TAG, "$functionName : GET News Data fail")
            }

        })

        val adapter = NewsRecyclerAdapter(this, newsList)
        news_recycler.adapter = adapter
        news_recycler.setHasFixedSize(true)
        news_recycler.layoutManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
    }

    companion object{
        const val TAG = "NewsActivity"
    }
}