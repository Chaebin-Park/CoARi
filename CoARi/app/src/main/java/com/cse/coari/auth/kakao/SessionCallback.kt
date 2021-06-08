package com.cse.coari.auth.kakao

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.cse.coari.activity.HomeActivity
import com.cse.coari.activity.LoginActivity
import com.cse.coari.data.LoginData
import com.cse.coari.data.LoginType
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException

class SessionCallback(val context: LoginActivity): ISessionCallback{
    override fun onSessionOpened() {
        UserManagement.getInstance().me(object: MeV2ResponseCallback(){
            override fun onSuccess(result: MeV2Response) {
                Log.i("KAKAO", "User Data : ${result.toString()}")

                val token = result.id
                val name = result.kakaoAccount.profile.nickname
                val email = result.kakaoAccount.email
                val profImg = result.kakaoAccount.profile.thumbnailImageUrl

                val data = LoginData(LoginType.KAKAO, name, "NONE", profImg, token.toString())

                val intent = Intent(context, HomeActivity::class.java).apply {
                    putExtra("LOGIN", data)
                }

                context.startActivity(intent)
                (context as Activity).finish()
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.e("Kakao", "SessionCallBack :: onSessionClosed: ${errorResult?.errorMessage}")
            }
        })
    }

    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Kakao", "SessionCallBack :: onSessionOpenFailed: ${exception?.message}")
    }
}