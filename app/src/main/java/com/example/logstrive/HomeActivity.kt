package com.example.logstrive

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_home_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up Bottom Navigation View with NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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
}