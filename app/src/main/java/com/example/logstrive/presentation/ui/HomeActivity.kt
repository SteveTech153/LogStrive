package com.example.logstrive.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.logstrive.R
import com.example.logstrive.util.NavigationConstants
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_home_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_home

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    if(navController.currentDestination?.id != R.id.homeFragment){
                        navController.navigate(R.id.homeFragment)
                    }
                }
                R.id.nav_graph -> {
                    if(navController.currentDestination?.id != R.id.graphFragment){
                        navController.navigate(R.id.graphFragment)
                    }
                }
                R.id.nav_habits -> {
                    if(navController.currentDestination?.id != R.id.habitFragment){
                        navController.navigate(R.id.habitFragment)
                    }
                }
                R.id.nav_lookback -> {
                    if(navController.currentDestination?.id != R.id.lookbackFragment){
                        navController.navigate(R.id.lookbackFragment)
                    }
                }
                R.id.nav_profile -> {
                    if(navController.currentDestination?.id != R.id.profileFragment){
                        navController.navigate(R.id.profileFragment)
                    }
                }
            }
            true
        }
    }

    fun navigateTo(nameOfTheFragment: String){
        when(nameOfTheFragment){
            NavigationConstants.HOME ->  {
                navController.navigate(R.id.homeFragment)
                bottomNavigationView.selectedItemId = R.id.nav_home
            }
            NavigationConstants.HABITS -> {
                navController.navigate(R.id.habitFragment)
                bottomNavigationView.selectedItemId = R.id.nav_habits
            }
            NavigationConstants.PROFILE -> {
                navController.navigate(R.id.profileFragment)
                bottomNavigationView.selectedItemId = R.id.nav_profile
            }
            NavigationConstants.GRAPH -> {
                navController.navigate(R.id.graphFragment)
                bottomNavigationView.selectedItemId = R.id.nav_graph
            }
            NavigationConstants.PROGRESS -> {
                navController.navigate(R.id.profileFragment)
                bottomNavigationView.selectedItemId = R.id.nav_lookback
            }
            else -> {
                navController.navigate(R.id.homeFragment)
                bottomNavigationView.selectedItemId = R.id.nav_home
            }
        }
    }



//    fun updateSelectedItemIdInBottomNavigationView(nameOfTheFragment: String){
//        when(nameOfTheFragment){
//            NavigationConstants.HOME ->  bottomNavigationView.selectedItemId = R.id.nav_home
//            NavigationConstants.HABITS -> bottomNavigationView.selectedItemId = R.id.nav_habits
//            NavigationConstants.PROFILE -> bottomNavigationView.selectedItemId = R.id.nav_profile
//            NavigationConstants.GRAPH -> bottomNavigationView.selectedItemId = R.id.nav_graph
//            NavigationConstants.PROGRESS -> bottomNavigationView.selectedItemId = R.id.nav_lookback
//            else -> bottomNavigationView.selectedItemId = R.id.nav_home
//        }
//    }
//    private fun setCurrFragment(fragment : Fragment){
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.my_home_nav_host_fragment,fragment)
//            commit()
//        }
//    }
}