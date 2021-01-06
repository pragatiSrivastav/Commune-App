package com.pragati.communeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.pragati.communeapp.R
import com.pragati.communeapp.fragment.TakeAttendanceFragment
import com.pragati.communeapp.fragment.ViewAttendanceFragment
import com.pragati.communeapp.fragment.AddClassFragment
import com.pragati.communeapp.fragment.ChangePasswordFragment
import com.pragati.communeapp.fragment.DashboardFragment
import com.pragati.communeapp.fragment.LogoutFragment
import com.pragati.communeapp.util.ConnectionManager

class MainActivity : AppCompatActivity() {

    lateinit var btnLogout : Button
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar : Toolbar
    lateinit var frame : FrameLayout
    lateinit var navigationHeader : NavigationView
    var previousMenuItem:   MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout      =   findViewById(R.id.drawerLayout)
        coordinateLayout  =   findViewById(R.id.CoordinateLayout)
        toolbar           =   findViewById(R.id.toolbar)
        frame             =   findViewById(R.id.frame)
        navigationHeader  =   findViewById(R.id.NavigationHeader)

        setUpToolbar()
        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(    //
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        navigationHeader.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it


            when(it.itemId){
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }

                R.id.Class -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AddClassFragment()
                        )
                        .commit()

                    supportActionBar?.title="Add Class"
                    drawerLayout.closeDrawers()

                }

                R.id.TakeAtt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            TakeAttendanceFragment()
                        )
                        .addToBackStack("Take Attendance")
                        .commit()
                    supportActionBar?.title="Take Attendance"
                    drawerLayout.closeDrawers()
                   // Toast.makeText(this@MainActivity,"Take attendance is clicked",Toast.LENGTH_SHORT).show()
                }

                R.id.ViewAtt -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ViewAttendanceFragment()
                        )
                        .commit()
                    supportActionBar?.title="View Attendance"
                    drawerLayout.closeDrawers()
                  //  Toast.makeText(this@MainActivity,"View Attendance is clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.ChangePass -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ChangePasswordFragment()
                        )
                        .commit()

                    supportActionBar?.title="Change Password"
                    drawerLayout.closeDrawers()
                  //  Toast.makeText(this@MainActivity,"Change password clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.Logout -> {

                    val intent = Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(intent)
//                    supportFragmentManager.beginTransaction()
//                        .replace(
//                            R.id.frame,
//                            LogoutFragment()
//                        )
//                        .commit()
//
//                    supportActionBar?.title="Logout"
//                    drawerLayout.closeDrawers()
                   // Toast.makeText(this@MainActivity,"Logout is clicked",Toast.LENGTH_SHORT).show()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        actionBarDrawerToggle.syncState()

        if(!ConnectionManager().checkConnectivity(this@MainActivity)){
            val dialog = android.app.AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                //do nothing)
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                //do nothing)
                ActivityCompat.finishAffinity(this@MainActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Commune"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)

    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationHeader.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else ->  super.onBackPressed()
        }
    }



}