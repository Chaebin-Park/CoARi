package com.cse.coari

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.cse.coari.auth.kakao.KakaoSDKAdapter
import com.kakao.auth.KakaoSDK
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility.getKeyHash
import com.kakao.util.helper.CommonProtocol
import com.kakao.util.helper.Utility

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        instance = this
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): GlobalApplication{
        checkNotNull(instance){
            "This Application does not inherit com.kakao.GlobalApplication"
        }
        return instance!!
    }


    companion object {
        var instance: GlobalApplication? = null
    }
}