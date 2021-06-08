package com.cse.coari.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.cse.coari.R
import com.cse.coari.adapter.ItemDecoration
import com.cse.coari.adapter.SwipeListAdapter
import com.cse.coari.data.AlarmDTO
import com.cse.coari.helper.SwipeHelperCallback
import com.cse.coari.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_alarm.*
import kotlinx.android.synthetic.main.item_alarm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class AlarmActivity : AppCompatActivity() {

    private lateinit var list: AlarmDTO
    private lateinit var adapter: SwipeListAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        val functionName = object {}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Activity Start")

        setAlarms()

        alarm_refresh.setOnRefreshListener {
            setAlarms()
            adapter.setData(list)
            adapter.notifyDataSetChanged()
            this.alarm_refresh.isRefreshing = false
        }
    }

    private fun setAlarms(){
        val functionName = object {}.javaClass.enclosingMethod.name

        RetrofitBuilder.api.getAlarm().enqueue(object : Callback<AlarmDTO> {
            @SuppressLint("ClickableViewAccessibility")
            override fun onResponse(call: Call<AlarmDTO>, response: Response<AlarmDTO>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "$functionName : GET Alarm Data success")
                    list = response.body()!!
                    adapter = SwipeListAdapter(this@AlarmActivity, list)
                    val swipeHelperCallback = SwipeHelperCallback().apply {
                        setClamp(300f)
                    }
                    val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
                    itemTouchHelper.attachToRecyclerView(alarm_recycler_view)

                    val animator = alarm_recycler_view.itemAnimator
                    if (animator is SimpleItemAnimator) {
                        animator.supportsChangeAnimations = false
                    }

                    alarm_recycler_view.layoutManager = LinearLayoutManager(this@AlarmActivity)
                    alarm_recycler_view.adapter = adapter
                    alarm_recycler_view.addItemDecoration(ItemDecoration())
                    alarm_recycler_view.setOnTouchListener { _, _ ->
                        swipeHelperCallback.removePreviousClamp(alarm_recycler_view)
                        false
                    }
                }
            }

            override fun onFailure(call: Call<AlarmDTO>, t: Throwable) {
                Log.i(TAG, "$functionName : GET Alarm Data fail {$t}")
            }

        })
    }

    fun test(){
        Log.e("e", "")
    }

    companion object{
        const val TAG = "AlarmActivity"
    }
}