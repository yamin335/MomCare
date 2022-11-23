package com.mom2b.androidApp.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.MainActivityBinding
import com.mom2b.androidApp.ui.base.BaseActivity
import com.mom2b.androidApp.utils.NavigationHost
import com.mom2b.androidApp.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

interface ShowHideBottomNavCallback {
    fun showOrHideBottomNav(showHide: Boolean)
}

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityBinding, MainActivityViewModel>(), NavigationHost, ShowHideBottomNavCallback {
    private lateinit var binding: MainActivityBinding
    override val viewModel: MainActivityViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val fragmentWithoutBottomNav = setOf(
        R.id.homeFragment,
        R.id.servicesFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        binding.showBottomNav = true

        // Setup multi-backStack supported bottomNav
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

//        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                onBackPressedDispatcher.onBackPressed()
//            }
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        //return currentNavController?.value?.navigateUp() ?: false || super.onSupportNavigateUp()
        return navController.navigateUp(appBarConfiguration)
    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        // Now that BottomNavigationBar has restored its instance state
//        // and its selectedItemId, we can proceed with setting up the
//        // BottomNavigationBar with Navigation
//        setupBottomNavigationBar()
//    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
//        setSupportActionBar(toolbar)
//        currentNavController?.value?.let {
//            setupActionBarWithNavController(it)
//        }
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.servicesFragment,  R.id.moreFragment)
        )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            hideKeyboard()
            //binding.showBottomNav = destination.id !in fragmentWithoutBottomNav
        }
    }

//    override fun onBackPressed() {
//        onBackPressedDispatcher.onBackPressed()
//    }

    override fun showOrHideBottomNav(showHide: Boolean) {
        binding.showBottomNav = showHide
    }
}