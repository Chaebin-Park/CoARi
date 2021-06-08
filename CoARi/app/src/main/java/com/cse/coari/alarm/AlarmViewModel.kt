package com.cse.coari.alarm

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cse.coari.data.AlarmDTO
import com.cse.coari.helper.RecyclerViewDecoration
import com.cse.coari.helper.SwipeHelperCallback

class AlarmViewModel(application: Application): AndroidViewModel(application) {
    private var alarmResult: MutableLiveData<AlarmDTO> = MutableLiveData<AlarmDTO>()
    private var alarmData: AlarmDTO = AlarmDTO()
    private val repo: AlarmRepo = AlarmRepo(application)
    private var alarmAdapter = AlarmAdapter(this)

    @SuppressLint("StaticFieldLeak")
    private lateinit var recycler : RecyclerView

    @SuppressLint("CheckResult")
    fun getAlarms(){
        repo.getAlarms().subscribe({
            Log.e("__ALARM", "AlarmViewModel : get alarm")
            for (i in it.indices){
                Log.e("__ALARM", "AlarmViewModel : get alarm : {${it[i]}}")
                alarmData.add(it[i])
                Handler(Looper.getMainLooper()).post {
                    alarmAdapter.notifyDataSetChanged()
                }
                if(alarmData.size <= 0){
                    Log.e("AlarmViewModel", "No Alarm")
                }
            }
            alarmResult.value = alarmData
        }, { t -> Log.e("__ALARM", "getAlarms throw {$t}")})
    }

    fun deleteAlarm(id: String){
        repo.deleteAlarms(id).subscribe({
            Log.e("__ALARM", "AlarmViewModel : delete {$id} alarm")
        }, { t ->
            Log.e("__ALARM", "deleteAlarm throw {$t}")
            for (i in alarmData.indices){
                Log.e("__ALARM", "AlarmViewModel in loop : check {$id} alarm")
                if(alarmData.size <= 0) {
                    Log.e("AlarmViewModel", "No Alarm")
                }
                if (alarmData[i].alarmId.toString() == id) {
                    Log.e("__ALARM", "AlarmViewModel in loop : delete {$alarmData} alarm")
                    alarmData.remove(alarmData[i])
                    break
                }
            }

            alarmAdapter.notifyDataSetChanged()
            recycler.adapter =alarmAdapter
            recycler.layoutManager = LinearLayoutManager(getApplication())
        })
    }

    fun refresh(){
        Log.e("__ALARM", "AlarmViewModel : refresh(), notifyDataSetChanged")
        alarmData.clear()
        getAlarms()
        alarmAdapter.notifyDataSetChanged()
    }

    fun getAlarmDTO(): AlarmDTO {
        return alarmData
    }

    fun viewInit(recyclerView: RecyclerView){
        recycler = recyclerView
        recycler.adapter = alarmAdapter
        recycler.layoutManager = LinearLayoutManager(getApplication())
        recycler.addItemDecoration(RecyclerViewDecoration(30))
        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(300f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}