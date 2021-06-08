package com.cse.coari.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cse.coari.R
import com.cse.coari.adapter.NoticeRecyclerAdapter
import com.cse.coari.data.NoticeDTO
import com.cse.coari.data.NoticeData
import com.cse.coari.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_notice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NoticeActivity : AppCompatActivity() {

    private lateinit var noticeList: NoticeDTO
    private val searchList: NoticeDTO = NoticeDTO()
    private lateinit var adapter: NoticeRecyclerAdapter

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        val functionName = object {}.javaClass.enclosingMethod.name

        // retrofit
        RetrofitBuilder.api.getNotice().enqueue(object : Callback<NoticeDTO> {
            override fun onResponse(
                call: Call<NoticeDTO>,
                response: Response<NoticeDTO>
            ) {
                if (response.isSuccessful) {
                    noticeList = response.body()!!
                    searchList.addAll(noticeList)

                    adapter = NoticeRecyclerAdapter(this@NoticeActivity, noticeList)
                    notice_recycler.adapter = adapter

                    val layout = LinearLayoutManager(this@NoticeActivity)
                    notice_recycler.layoutManager = layout
                    notice_recycler.setHasFixedSize(true)
                    Log.i(TAG, "$functionName : Get notice data success")
                }
            }

            override fun onFailure(call: Call<NoticeDTO>, t: Throwable) {
                Log.i(TAG, "$functionName : Get notice data fail")
            }

        })

        notice_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i(TAG, "beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i(TAG, "onTextChanged")
            }

            override fun afterTextChanged(s: Editable?) {
                val text = notice_edit_text.text.toString()
                search(text)
            }
        })

        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        notice_edit_text.setOnEditorActionListener { textView, actionId, event ->
            var handled = false

            val isEnterEvent = event != null && event.keyCode == KeyEvent.KEYCODE_ENTER
            val isEnterUpEvent = isEnterEvent && event?.action == KeyEvent.ACTION_UP

            if (actionId == EditorInfo.IME_ACTION_SEARCH || isEnterUpEvent) {
                Log.e(TAG, "검색버튼 누름")
                imm.hideSoftInputFromWindow(notice_edit_text.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                search(textView.text.toString())
                notice_edit_text.apply {
                    clearFocus()
                    isFocusable = false
                    isFocusableInTouchMode = true
                    isFocusable = true
                }
                handled = true
            }
            handled
        }
    }

    override fun onBackPressed() {
        if(notice_edit_text.hasFocus()){
            notice_edit_text.apply {
                clearFocus()
                isFocusable = false
                setText("")
                isFocusableInTouchMode = true
                isFocusable = true
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun search(text: String) {
        noticeList.clear()

        if (text.isEmpty()) {
            noticeList.addAll(searchList)
        } else {
            for (i in searchList.indices step (1)) {
                if (searchList[i].title.toLowerCase(Locale.ROOT).contains(text)) {
                    noticeList.add(searchList[i])
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "NoticeActivity"
    }
}