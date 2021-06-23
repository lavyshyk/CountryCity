package com.lavyshyk.countrycity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lavyshyk.countrycity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //val navHostFragment = binding.fragmentContainerView.let { supportFragmentManager } as NavHostFragment // app not work ????
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = binding.bottomNavView
        bottomNav?.setupWithNavController(navController)
    }
}