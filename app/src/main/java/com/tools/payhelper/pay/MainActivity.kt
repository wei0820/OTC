package com.jingyu.pay

import android.os.Bundle
import android.util.Log
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tools.payhelper.R
import com.tools.payhelper.databinding.ActivityMainBinding
import com.tools.payhelper.pay.PayHelperUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  val TAG = "MainActivity"
    var googleBoolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_notifications,  R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_order, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.selectedItemId = R.id.navigation_notifications
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    fun  getData() : Boolean{
        return  intent.getBooleanExtra("google",false)

    }
}