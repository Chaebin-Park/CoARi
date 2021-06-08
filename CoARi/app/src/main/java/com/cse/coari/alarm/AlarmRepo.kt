package com.cse.coari.alarm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cse.coari.data.AlarmDTO
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers.io

class AlarmRepo(application: Application) {
    private val api = AlarmAPI.create()
    private var alarmData: MutableLiveData<AlarmDTO> = MutableLiveData()

    fun getAlarms(): Observable<AlarmDTO>
    = api.getAlarms()
        .subscribeOn(io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteAlarms(id: String): Observable<AlarmDTO>
    = api.deleteAlarm(id)
        .subscribeOn(io())
        .observeOn(AndroidSchedulers.mainThread())
}