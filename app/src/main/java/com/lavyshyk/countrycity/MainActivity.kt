package com.lavyshyk.countrycity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lavyshyk.countrycity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root



        setContentView(view)

        //val navHostFragment = binding.fragmentContainerView.let { supportFragmentManager } as NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController
        val toolBar = binding.toolBar
        setSupportActionBar(toolBar)
        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav: BottomNavigationView = binding.bottomNavView
        bottomNav.setupWithNavController(navController)
//        bottomNav.setOnNavigationItemSelectedListener{
//            when (it.itemId) {
//                R.id.start_fragment_Bmenu -> R.id.startFragment
//                R.id.list_fragment_Bmenu -> R.id.listFragment
//                R.id.detail_fragment_Bmenu -> {
//                    R.id.listFragment
//                }
//            }
//            return@setOnNavigationItemSelectedListener true
//        }
    }

}





