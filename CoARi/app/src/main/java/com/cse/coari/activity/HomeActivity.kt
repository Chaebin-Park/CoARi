package com.cse.coari.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.cse.coari.R
import com.cse.coari.data.LoginData
import com.cse.coari.data.LoginType
import com.cse.coari.alarm.AlarmMainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.kakao.auth.Session
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_test.btn_tour
import kotlinx.android.synthetic.main.activity_test.layout_curr
import kotlinx.android.synthetic.main.activity_test.layout_emp
import kotlinx.android.synthetic.main.activity_test.layout_info
import kotlinx.android.synthetic.main.activity_test.layout_news
import kotlinx.android.synthetic.main.activity_test.layout_notice
import kotlinx.android.synthetic.main.activity_test.layout_prof
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.menu_header.view.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var exitResult: String

    // Layout members
    private lateinit var drawerToggle : ActionBarDrawerToggle
    private lateinit var loginType : LoginType
    private lateinit var loginData : LoginData

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val functionName = object{}.javaClass.enclosingMethod.name

        Log.i(TAG, "$functionName : Activity Start")

        if(intent.hasExtra("LOGIN")){
            loginData = intent.getSerializableExtra("LOGIN") as LoginData
            Log.i(TAG, "$functionName : Intent Login Data $loginData")
        }

        setSupportActionBar(custom_toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher_round)

        drawerToggle = ActionBarDrawerToggle(
            this,
            dl_drawer_root,
            custom_toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        dl_drawer_root.addDrawerListener(drawerToggle)
        nv_main_navigation_root.setNavigationItemSelectedListener(this)

        val navigationView = nv_main_navigation_root.getHeaderView(0)

        when(loginData.type){
            LoginType.GOOGLE -> {
                navigationView.tv_drawer_id.text = loginData.name
                navigationView.tv_drawer_email.text = loginData.email
                navigationView.iv_drawer_profile.setRadius(50.0f)
                Glide.with(this).load(loginData.profImg).into(navigationView.iv_drawer_profile)
            }
            else -> {
                navigationView.tv_drawer_id.text = loginData.name
                navigationView.tv_drawer_email.visibility = INVISIBLE
                navigationView.iv_drawer_profile.setRadius(50.0f)
                Glide.with(this).load(loginData.profImg).into(navigationView.iv_drawer_profile)

            }
        }

        btn_tour.setOnClickListener{
            onClickMenu(TOUR)
        }

        layout_info.setOnClickListener{
            onClickMenu(INFO)
        }

        layout_notice.setOnClickListener{
            onClickMenu(NOTICE)
        }

        layout_news.setOnClickListener{
            onClickMenu(NEWS)
        }

        layout_prof.setOnClickListener{
            onClickMenu(PROF)
        }

        layout_curr.setOnClickListener{
            onClickMenu(CURR)
        }

        layout_emp.setOnClickListener{
            onClickMenu(EMP)
        }
    }

    /*
    * Click Listeners
    * Change Activity intent
    */

    private fun onClickLogout(type: LoginType){
        val functionName = object{}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Logout Button Clicked")
        val intent = Intent(this, LoginActivity::class.java)
        when(type){
            LoginType.KAKAO -> {
                Log.i(TAG, "$functionName : Kakao Logout Process. Go to LoginActivity")
                Session.getCurrentSession().close()
                startActivity(intent)
            }
            LoginType.GOOGLE -> {
                Log.i(TAG, "$functionName : Google Logout Process. Go to LoginActivity")
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.signOut()
                startActivity(intent)
            }
            else -> {
                Log.i(TAG, "GUEST OR ADMIN")
            }
        }
    }

    private fun onClickMenu(menu: String){
        val functionName = object{}.javaClass.enclosingMethod.name
        val intent: Intent

        when(menu){
            INFO -> {
                Log.i(TAG, "$functionName : Info button clicked. Go to InfoActivity")
                intent = Intent(this, InfoActivity::class.java)
            }
            NOTICE -> {
                Log.i(TAG, "$functionName : Notice button clicked. Go to NoticeActivity")
                intent = Intent(this, NoticeActivity::class.java)
            }
            NEWS -> {
                Log.i(TAG, "$functionName : News button clicked. Go to NewsActivity")
                intent = Intent(this, NewsActivity::class.java)
            }
            PROF -> {
                Log.i(TAG, "$functionName : Prof button clicked. Go to ProfActivity")
                intent = Intent(this, ProfInfoActivity::class.java)
            }
            CURR -> {
                Log.i(TAG, "$functionName : Curr button clicked. Go to CurrActivity")
                intent = Intent(this, CurriculumActivity::class.java)
            }
            EMP -> {
                Log.i(TAG, "$functionName : Emp button clicked. Go to EmpActivity")
                intent = Intent(this, EmpActivity::class.java)
            }
            TOUR -> {
                Log.i(TAG, "$functionName : Tour button clicked. Go to TourActivity")
                intent = Intent(this, TourActivity::class.java)
            }
            else -> {
                Log.i(TAG, "$functionName : Home button clicked. Go to HomeActivity")
                intent = Intent(this, HomeActivity::class.java)
            }
        }
        startActivity(intent)
    }

    /*
     * override Backpress action
     * if Drawer opened, close drawer
     * if Drawer closes, exit popup start
     */
    override fun onBackPressed() {
        val functionName = object{}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Back button clicked.")

        if(dl_drawer_root.isDrawerOpen((GravityCompat.START))){
            Log.i(TAG, "$functionName : Drawer close.")
            dl_drawer_root.closeDrawer(GravityCompat.START)
        } else {
            Log.i(TAG, "$functionName : Call close Application popup")
            mPopupClick()
        }
    }

    /*
     * exit popup start
     */
    private fun mPopupClick(){
        val functionName = object{}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Shutdown popup open")
        val intent = Intent(this, ExitPopupActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val functionName = object{}.javaClass.enclosingMethod.name
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Log.i(TAG, "$functionName : Shutdown application")
                moveTaskToBack(true)
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            else if (resultCode == RESULT_CANCELED){
                Log.i(TAG, "$functionName : Shutdown popup canceled")
            }
        }
    }

    /*
     * drawer item click events
     * if alarm menu clicked, start AlarmActivity
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val functionName = object{}.javaClass.enclosingMethod.name
        when(item.itemId){
            R.id.menu_alarm -> {
                if(loginData.type == LoginType.KAKAO || loginData.type == LoginType.GOOGLE) {
                    Log.i(TAG, "$functionName : Alarm menu clicked")
                    val intent = Intent(this, AlarmMainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "로그인을 해야 이용할 수 있는 서비스입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.menu_logout -> {
                if(loginData.type == LoginType.KAKAO || loginData.type == LoginType.GOOGLE) {
                    Log.i(TAG, "$functionName : Logout menu clicked")
                    onClickLogout(loginData.type)
                } else {
                    Toast.makeText(this, "로그인 화면으로 나갑니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
            else -> {
                Log.i(TAG, "$functionName : How??")
            }
        }
        dl_drawer_root.closeDrawer(GravityCompat.START)
        return false
    }

    /*
     * sync drawer state
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val functionName = object{}.javaClass.enclosingMethod.name
        Log.i(TAG, "$functionName : Sync drawer state")
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        val functionName = object{}.javaClass.enclosingMethod.name
        return if (drawerToggle.onOptionsItemSelected(item)) {
            Log.i(TAG, "$functionName : Drawer selected")
            true
        } else {
            super.onOptionsItemSelected(item)
        }
        // Handle your other action bar items...
    }

    companion object {
        const val TAG       = "HomeActivity"
        const val INFO      = "INFO"
        const val NOTICE    = "NOTICE"
        const val NEWS      = "NEWS"
        const val PROF      = "PROF"
        const val CURR      = "CURR"
        const val EMP       = "EMP"
        const val TOUR      = "TOUR"
    }
}