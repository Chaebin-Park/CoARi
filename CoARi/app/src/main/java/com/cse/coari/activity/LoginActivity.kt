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
import com.cse.coari.data.LoginType
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.admin_login_dialog.view.*
import kotlin.math.sign


class LoginActivity : AppCompatActivity() {

//    private lateinit var mTouchEvent: View.OnTouchListener
    private val id = "admin"
    private val pw = "admin"
    private lateinit var checkId: String
    private lateinit var checkPw: String
    private var callback: SessionCallback = SessionCallback(this)

    private lateinit var auth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val adminLogin = findViewById<ImageButton>(R.id.btnAdminLogin)
        val linear : LinearLayout = View.inflate(this, R.layout.admin_login_dialog, null) as LinearLayout
        val intent = Intent(this, HomeActivity::class.java)

        // 카카오 로그인
        layoutKakaoLogin.setOnClickListener{kakaoLogin()}

        // 어드민 로그인
        adminLogin.setOnClickListener{
            val btnInput = linear.btn_admin_login
            val btnCancel = linear.btn_admin_cancel
            val editId = linear.et_admin_id
            val editPw = linear.et_admin_pw

            if(linear.parent != null){
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
                if(checkPw == pw && checkId == id){
                    ad.dismiss()
                    intent.putExtra("CASE", LoginType.ADMIN)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Wrong Account!", Toast.LENGTH_SHORT).show()
                }
            }

            btnCancel.setOnClickListener{
                ad.dismiss()
            }
        }

        // 구글 로그인
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        layoutGoogleLogin.setOnClickListener{
            signIn()
        }

        // 게스트 로그인
        layoutGuestLogin.setOnClickListener{
            intent.putExtra("CASE", LoginType.GUEST)
            startActivity(intent)
        }
    }

    // 카카오 로그인
    private fun kakaoLogin(){
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
    }

    // 구글 로그인

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginSuccess(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){ task->
            if(task.isSuccessful){
                val user = auth.currentUser
                loginSuccess()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 카카오 로그인 확인
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            Log.i("Kakao", "session get current session")
            return
        }

        // 구글 로그인 확인
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException){

            }
        }
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 카카오 세션 종료
        Session.getCurrentSession().removeCallback(callback)
    }
}