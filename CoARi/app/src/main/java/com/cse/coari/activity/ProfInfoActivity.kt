package com.cse.coari.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cse.coari.R
import com.cse.coari.adapter.ProfRecyclerAdapter
import com.cse.coari.data.ProfData
import kotlinx.android.synthetic.main.activity_prof_info.*

class ProfInfoActivity : AppCompatActivity() {

    private var profList = arrayListOf<ProfData>(
        ProfData("prof_ojkwon", "권오준", "ojkwon@deu.ac.kr", "인공지능, 컴퓨터 네트워크, 정보 보호", "051-890-1725", "정보공학관 903호"),
        ProfData("prof_skkwon", "권순각", "skkwon@deu.ac.kr", "멀티미디어 정보처리, 영상통신", "051-890-1727", "정보공학관 904호"),
        ProfData("prof_swkim", "김성우", "libero@deu.ac.kr", "임베디드시스템, 운영체제", "051-890-1728", "정보공학관 905호"),
        ProfData("prof_jhlee", "이중화", "junghwa@deu.ac.kr", "데이터베이스, 자료구조, 웹 시스템", "051-890-1729", "정보공학관 906호"),
        ProfData("prof_jmlee", "이종민", "jongmin@deu.ac.kr", "모바일 컴퓨팅, 병렬처리(GPU컴퓨팅), S/W공학, OOAD", "051-890-1736", "정보공학관 907호"),
        ProfData("prof_yhpark", "박유현", "yhpark@deu.ac.kr", "인터넷 시스템, 클러스터 시스템", "051-890-1737", "정보공학관 901호"),
        ProfData("prof_yhleem", "임영호", "yhleem@deu.ac.kr", "컴퓨터 응용, 컨텐츠 제작 기술", "051-890-1847", "정보공학관 808호"),
        ProfData("prof_hsjang", "장희숙", "jang1052@deu.ac.kr", "소프트웨어공학, 시스템 분석 설계, 네트워크 프로토콜", "051-890-1815", "정보공학관 701호")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prof_info)

        val adapter = ProfRecyclerAdapter(this, profList)
        prof_info_recycler.adapter = adapter

        val layout = GridLayoutManager(this, 2)
        prof_info_recycler.layoutManager = layout
        prof_info_recycler.setHasFixedSize(true)
    }
}