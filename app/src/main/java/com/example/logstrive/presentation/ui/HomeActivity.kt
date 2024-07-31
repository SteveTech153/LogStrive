package com.example.logstrive.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.logstrive.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_home_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up Bottom Navigation View with NavController
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.selectedItemId = R.id.nav_home

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> setCurrFragment(HomeFragment())
//                R.id.nav_graph ->
//                    setCurrFragment(G)
                R.id.nav_habits -> setCurrFragment(HabitFragment())
            }
            true
        }
    }
    private fun setCurrFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.my_home_nav_host_fragment,fragment)
            commit()
        }
    }
    fun updateSelectedItemIdInBottomNavigationView(nameOfTheFragment: String){
        when(nameOfTheFragment){
            "home" ->  bottomNavigationView.selectedItemId = R.id.nav_home
            "habits" -> bottomNavigationView.selectedItemId = R.id.nav_habits
            "profile" -> bottomNavigationView.selectedItemId = R.id.nav_profile
            "graph"-> bottomNavigationView.selectedItemId = R.id.nav_graph
            "progress"-> bottomNavigationView.selectedItemId = R.id.nav_progress
            else -> bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }
}