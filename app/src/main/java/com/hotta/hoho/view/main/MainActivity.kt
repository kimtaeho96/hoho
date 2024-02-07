package com.hotta.hoho.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityMainBinding
import com.hotta.hoho.utils.AppPreferences
import com.hotta.hoho.view.map.MapActivity
import com.hotta.hoho.view.adapter.DayMovieAdapter

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private val TAG = "!!@@" + MainActivity::class.java.simpleName

    lateinit var rvAdapter: DayMovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mainTitle = findViewById<TextView>(R.id.mainTitle)
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.closer_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setHomeButtonEnabled(true)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val headerView: View = navigationView.getHeaderView(0)
        val navName = headerView.findViewById<TextView>(R.id.nav_Name)
        navName.setText("이건가용")


        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainerView)

        Log.d("zxcvasd", navController.currentDestination?.id.toString())

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("zxcvasd", destination.id.toString())
            when (destination.id) {
                R.id.homeFragment1 -> mainTitle.setText("메인")
                R.id.homeFragment2 -> mainTitle.setText("영화")
                R.id.homeFragment3 -> mainTitle.setText("")


                /*R.id.fragment1 -> supportActionBar?.title = "Fragment 1"
                    R.id.fragment2 -> supportActionBar?.title = "Fragment 2"
                    R.id.fragment3 -> supportActionBar?.title = "Fragment 3"*/
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val mapIntent = Intent(this, MapActivity::class.java)
        when (item.itemId) {
            R.id.nav_home -> startActivity(mapIntent)

            R.id.nav_logout -> Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}