package com.cse.coari.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cse.coari.R
import com.cse.coari.auth.kakao.SessionCallback
import com.cse.coari.data.LoginData
import com.cse.coari.data.LoginType
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.kakao.sdk.common.model.AuthErrorCause.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.admin_login_dialog.view.*


@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginActivity : AppCompatActivity() {

    private val id = "admin"
    private val pw = "admin"
    private lateinit var checkId: String
    private lateinit var checkPw: String
    private var callback: SessionCallback = SessionCallback(this)
    private lateinit var firebaseAuth: FirebaseAuth

    private val googleSignInIntent by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(this, gso).signInIntent
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val functionName = object {}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Activity Start")

        val adminLogin = findViewById<ImageButton>(R.id.btnAdminLogin)
        val linear: LinearLayout =
            View.inflate(this, R.layout.admin_login_dialog, null) as LinearLayout
        val intent = Intent(this, HomeActivity::class.java)

        // 카카오 로그인
        layoutKakaoLogin.setOnClickListener {
            Log.i(TAG, "$functionName : Kakao login button clicked")
            kakaoLogin()
        }

        // 어드민 로그인
        adminLogin.setOnClickListener {
            Log.i(TAG, "$functionName : Admin login button clicked")
            val btnInput = linear.btn_admin_login
            val btnCancel = linear.btn_admin_cancel
            val editId = linear.et_admin_id
            val editPw = linear.et_admin_pw

            if (linear.parent != null) {
                (linear.parent as ViewGroup).removeView(linear)
            }

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Input Admin Account")
                .setView(linear)
                .setIcon(R.drawable.ic_deu_logo)
            val ad = builder.create()
            ad.show()

            btnInput.setOnClickListener {
                checkId = editId.text.toString()
                checkPw = editPw.text.toString()

                Log.i(TAG, "$functionName : Admin login data input id : $checkId , pw : $checkPw")

                if (checkPw == pw && checkId == id) {
                    Log.i(TAG, "$functionName : Admin login access")
                    ad.dismiss()
                    val data = LoginData(LoginType.ADMIN, "guest")
                    intent.putExtra("LOGIN", data)
                } else {
                    Log.i(TAG, "$functionName : Admin login fail")
                    Toast.makeText(this, "Wrong Account!", Toast.LENGTH_SHORT).show()
                }
            }

            btnCancel.setOnClickListener {
                Log.i(TAG, "$functionName : Admin login cancel")
                ad.dismiss()
            }
        }

        // 구글 로그인
        firebaseAuth = FirebaseAuth.getInstance()
        layoutGoogleLogin.setOnClickListener {
            Log.i(TAG, "$functionName : Google login button clicked")
            startActivityForResult(googleSignInIntent, RESULT_CODE)
        }

        // 게스트 로그인
        layoutGuestLogin.setOnClickListener {
            Log.i(TAG, "$functionName : Guest login button clicked")
            val data = LoginData(LoginType.GUEST, "guest")
            intent.putExtra("LOGIN", data)
            startActivity(intent)
        }
    }

    // 카카오 로그인
    private fun kakaoLogin() {
        val functionName = object {}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Kakao login process")
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
    }

    // 구글 로그인

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val functionName = object {}.javaClass.enclosingMethod.name

        // 카카오 로그인 확인
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i(TAG, "Session get current session")
            return
        }

        // 구글 로그인 확인
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_CODE) {
            Log.i(TAG, "$functionName : Check google login")
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                firebaseLogin(result.signInAccount!!)
            }
        }
    }

    private fun firebaseLogin(googleAccount: GoogleSignInAccount) {
        val functionName = object {}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Firebase login process")

        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(TAG, "$functionName : Firebase login success {${googleAccount.idToken}}")
                val name = it.result?.user?.displayName //사용자 이름
                val email = it.result?.user?.email
                val photo = it.result?.user?.photoUrl
                val token = it.result?.user?.uid
                Log.e(TAG, "$name $email $photo $token")
                val data =
                    LoginData(LoginType.GOOGLE, name!!, email!!, photo.toString(), token!!)
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("LOGIN", data)
                startActivity(intent)
            } else {
                Log.e(TAG, "Firebase login Failed")
                //error 처리
            }
        }.addOnFailureListener {
            Log.i(TAG, "$functionName : Firebase credential failed")
            //error 처리
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 카카오 세션 종료
        val functionName = object {}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Kakao session removed")
        Session.getCurrentSession().removeCallback(callback)
    }

    companion object {
        const val RESULT_CODE = 10
        const val TAG = "LoginActivity"
    }
}