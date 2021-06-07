package com.cse.coari.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cse.coari.R
import com.cse.coari.data.LoginType
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.menu_header.*
import kotlinx.android.synthetic.main.menu_header.view.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var exitResult: String

    // Layout members
    private lateinit var drawerToggle : ActionBarDrawerToggle
    private lateinit var loginType : LoginType

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(intent.hasExtra("CASE")){
            loginType = intent.getSerializableExtra("CASE") as LoginType
            Toast.makeText(this, loginType.toString(), Toast.LENGTH_SHORT).show()
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

        btn_tour.setOnClickListener{
            onClickTour(it)
        }

        layout_info.setOnClickListener{
            onClickInfo(it)
        }

        layout_notice.setOnClickListener{
            onClickNotice(it)
        }

        layout_news.setOnClickListener{
            onClickNews(it)
        }

        layout_prof.setOnClickListener{
            onClickProfInfo(it)
        }

        layout_curr.setOnClickListener{
            onClickCurr(it)
        }

        layout_emp.setOnClickListener{
            onClickEmp(it)
        }
    }

    /*
    * Click Listeners
    * Change Activity intent
    */
    private fun onClickTour(v: View){
        val intent = Intent(this, TourActivity::class.java)
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


    /*
     * override Backpress action
     * if Drawer opened, close drawer
     * if Drawer closes, exit popup start
     */
    override fun onBackPressed() {
        if(dl_drawer_root.isDrawerOpen((GravityCompat.START))){
            dl_drawer_root.closeDrawer(GravityCompat.START)
        } else {
            mPopupClick()
        }
    }

    /*
     * exit popup start
     */
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

    /*
     * drawer item click events
     * if alarm menu clicked, start AlarmActivity
     */
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
        dl_drawer_root.closeDrawer(GravityCompat.START)
        return false
    }

    /*
     * sync drawer state
     */
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