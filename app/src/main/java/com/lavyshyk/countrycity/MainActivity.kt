package com.lavyshyk.countrycity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lavyshyk.countrycity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root

        setContentView(view)
        //val navHostFragment = binding.fragmentContainerView.let { supportFragmentManager } as NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        //   NavigationUI.setupActionBarWithNavController(this,navController)

        setupBottomNavMenu(navController)
       // setSupportActionBar(binding.toolBar)

    }
    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = binding.bottomNavView
        bottomNav.setupWithNavController(navController)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
//            R.id.sortCountryFromBigToSmall -> {
//                true
//                //TODO
//            }
//            R.id.sortCountryFromSmallToBig -> {
//                //TODO
//                true
//            }
//        else -> {super.onOptionsItemSelected(item)}
//        }
   }





