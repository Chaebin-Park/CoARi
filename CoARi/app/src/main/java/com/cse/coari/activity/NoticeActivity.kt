package com.cse.coari.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cse.coari.R
import com.cse.coari.adapter.NoticeRecyclerAdapter
import com.cse.coari.data.NoticeData
import kotlinx.android.synthetic.main.activity_notice.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoticeActivity : AppCompatActivity() {

    var noticeList = arrayListOf<NoticeData>(
        NoticeData("title1", "content1content1content1content1content1content1content1content1content1content1content1", "20/05/12"),
        NoticeData("title2", "content2content2content2content2content2content2content2content2content2content2content2", "20/05/11"),
        NoticeData("title3", "content3content3content3content3content3content3content3content3content3content3content3", "20/05/10"),
        NoticeData("title4", "content4content4content4content4content4content4content4content4content4content4content4", "20/05/09"),
        NoticeData("title5", "content5content5content5content5content5content5content5content5content5content5content5", "20/05/08"),
        NoticeData("title6", "content6content6content6content6content6content6content6content6content6content6content6", "20/05/07"),
        NoticeData("title7", "content7content7content7content7content7content7content7content7content7content7content7", "20/05/06"),
        NoticeData("title8", "content8content8content8content8content8content8content8content8content8content8content8", "20/05/05"),
        NoticeData("title9", "content9content9content9content9content9content9content9content9content9content9content9", "20/05/04"),
        NoticeData("title10", "content10content10content10content10content10content10content10content10content10content10content10", "20/05/03"),
        NoticeData("title11", "content11content11content11content11content11content11content11content11content11content11content11", "20/05/02"),
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        val adapter = NoticeRecyclerAdapter(this, noticeList)
        notice_recycler.adapter = adapter

        val layout = LinearLayoutManager(this)
        notice_recycler.layoutManager = layout
        notice_recycler.setHasFixedSize(true)

//        notice_edittext.setOnEditorActionListener(object : TextView.OnEditorActionListener{
//            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH){
//                    editTextSearch()
//                    return true;
//                }
//                return false
//            }
//        })
    }

    private fun editTextSearch(){

    }
}