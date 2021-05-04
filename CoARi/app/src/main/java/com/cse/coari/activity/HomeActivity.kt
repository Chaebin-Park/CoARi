package com.cse.coari.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cse.coari.R
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var exitResult: String

    private lateinit var btnTour : Button
    private lateinit var layoutInfo : LinearLayout
    private lateinit var layoutNotice : LinearLayout
    private lateinit var layoutNews : LinearLayout
    private lateinit var layoutProf : LinearLayout
    private lateinit var layoutCurr : LinearLayout
    private lateinit var layoutEmp : LinearLayout
    private lateinit var ivMenu : ImageView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var drawerToggle : ActionBarDrawerToggle
    private lateinit var navigationView : NavigationView
    private lateinit var toolbar : Toolbar

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.custom_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher_round)

        btnTour = findViewById(R.id.btn_tour)
        layoutInfo = findViewById(R.id.layout_info)
        layoutNotice = findViewById(R.id.layout_notice)
        layoutNews = findViewById(R.id.layout_news)
        layoutProf = findViewById(R.id.layout_prof)
        layoutCurr = findViewById(R.id.layout_curr)
        layoutEmp = findViewById(R.id.layout_emp)

        drawerLayout = findViewById(R.id.dl_drawer_root)
        navigationView = findViewById(R.id.nv_main_navigation_root)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        navigationView.setNavigationItemSelectedListener(this)


        btnTour.setOnClickListener{
            onClickTour(it)
        }

        layoutInfo.setOnClickListener{
            onClickInfo(it)
        }

        layoutNotice.setOnClickListener{
            onClickNotice(it)
        }

        layoutNews.setOnClickListener{
            onClickNews(it)
        }

        layoutProf.setOnClickListener{
            onClickProfInfo(it)
        }

        layoutCurr.setOnClickListener{
            onClickCurr(it)
        }

        layoutEmp.setOnClickListener{
            onClickEmp(it)
        }
    }

    private fun onClickTour(v: View){
        val intent = Intent(this, ArActivity::class.java)
        startActivity(intent)
    }

    private fun onClickInfo(v: View){
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickNotice(v: View){
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
    }

    private fun onClickNews(v: View){
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

    private fun onClickProfInfo(v: View){
        val intent = Intent(this, ProfInfoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickCurr(v: View){
        val intent = Intent(this, CurriculumActivity::class.java)
        startActivity(intent)
    }

    private fun onClickEmp(v: View){
        val intent = Intent(this, EmpActivity::class.java)
        startActivity(intent)
    }


    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            mPopupClick()
        }
    }

    private fun mPopupClick(){
        val intent = Intent(this, ExitPopupActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                moveTaskToBack(true)
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "stay", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_alarm -> {
                Toast.makeText(this, "alarm clicked..", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_item1 -> {
                Toast.makeText(this, "item1 clicked..", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_item2 -> {
                Toast.makeText(this, "item2 clicked..", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return if (drawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item!!)
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}