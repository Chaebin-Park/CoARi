package com.cse.coari.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cse.coari.R
import com.cse.coari.data.NoticeData
import kotlinx.android.synthetic.main.activity_detail_notice.*

class DetailNoticeActivity : AppCompatActivity() {

    private lateinit var noticeData: NoticeData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notice)

//        noticeData = intent.getSerializableExtra("notice_data") as NoticeData
//        detail_notice_title.text = noticeData.strTitle
//        detail_notice_context.text = noticeData.strContext
//        detail_notice_date.text = noticeData.strDate

        detail_notice_webview.loadUrl("https://cse.deu.ac.kr/bbs/board.php?bo_table=notice&wr_id=264")

    }
}