package com.cse.coari.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cse.coari.R
import com.cse.coari.adapter.NewsRecyclerAdapter
import com.cse.coari.data.NewsDTO
import com.cse.coari.data.NewsData
import com.cse.coari.retrofit.NewsRetrofitClient
import kotlinx.android.synthetic.main.activity_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    private val newsData = MutableLiveData<NewsDTO>()

    private val newsList = arrayListOf(
        NewsData("deu_logo", "2021 컴퓨터소프트웨어공학 봉사동아리 CPU, 3D펜 교육 봉사", "04-02", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=34"),
        NewsData("deu_logo", "전공 취업 동아리 네카라쿠배 졸업생 만남", "03-25", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=33"),
        NewsData("deu_logo", "컴퓨터소프트웨어공학 봉사동아리 CPU 동의동락 프로젝트", "02-08", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=31"),
        NewsData("deu_logo", "컴퓨터소프트웨어공학 동의대 LINC+사업단, B.SORI 장려상 수상", "01-19", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=30"),
        NewsData("deu_logo", "컴퓨터소프트웨어공학 4학년 김O현 학생, 부산미래과학자상 대학생 공학 부문 우수상 수상", "12-10", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=29"),
        NewsData("deu_logo", "컴퓨터소프트웨어공학과, '크라우드 테스팅 경진대회' 입상", "12-03", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=28"),
        NewsData("deu_logo", "2020 동행 봉사 인성함양과 컴퓨터소프트웨어공학과 전공 체험", "11-11", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=27"),
        NewsData("deu_logo", "컴퓨터소프트웨어공학 제 2회 동의동락(同意同樂) PROJECT", "11-02", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=26"),
        NewsData("deu_logo", "컴퓨터소프트웨어공학과 진로지도형 졸업생 만남 프로그램 2회차", "11-02", "https://cse.deu.ac.kr/bbs/board.php?bo_table=gallery&wr_id=25")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        getNewsData().observe(this, Observer {
            Log.i("RETRO", "something changed")
        })

        val adapter = NewsRecyclerAdapter(this, newsList)
        news_recycler.adapter = adapter
        news_recycler.setHasFixedSize(true)
        news_recycler.layoutManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
    }

    private fun getNewsData(): MutableLiveData<NewsDTO> {
        val call = NewsRetrofitClient.newsRetrofitService

        call.getNewsDTO().enqueue(object : Callback<NewsDTO> {
            override fun onResponse(call: Call<NewsDTO>, response: Response<NewsDTO>) {
                newsData.value = response.body() as NewsDTO
                Log.i("RETRO", "Success : {$response}")
            }

            override fun onFailure(call: Call<NewsDTO>, t: Throwable) {
                t.printStackTrace()
                Log.i("RETRO", "Fail : {$t}")
            }
        })

        return newsData
    }
}