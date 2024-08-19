package com.hotta.hoho.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.hotta.hoho.Const_Ho
import com.hotta.hoho.R
import com.hotta.hoho.Statics
import com.hotta.hoho.databinding.ActivityMainBinding
import com.hotta.hoho.utils.AppPreferences
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.view.map.MapActivity
import com.hotta.hoho.view.adapter.DayMovieAdapter
import com.hotta.hoho.view.custom.CustomPopup
import com.hotta.hoho.view.join.LoginActivity
import com.hotta.hoho.view.mymovie.MyLikeMovieActivity
import com.hotta.hoho.view.mypage.MyPageActivity
import com.hotta.hoho.view.myreview.MyReviewActivity
import com.hotta.hoho.view.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private val TAG = "!!@@" + MainActivity::class.java.simpleName

    lateinit var rvAdapter: DayMovieAdapter

    lateinit var navLogInOutItem: MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserInfo(FireBaseAuthUtils.getUid())


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mainTitle = findViewById<TextView>(R.id.mainTitle)
        val searchIcon = findViewById<ImageView>(R.id.search_Img)

        searchIcon.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        setSupportActionBar(toolbar)
        val nav = findViewById<NavigationView>(R.id.nav_view)

        drawerLayout = binding.drawerLayout


        val navigationView = binding.navView
        // navigationView.setNavigationItemSelectedListener(this)

        val navLogInOutItem = navigationView.menu.findItem(R.id.nav_login_logout)


        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.closer_nav
        )
//        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setHomeButtonEnabled(true)

        val headerView: View = navigationView.getHeaderView(0)
        val navName = headerView.findViewById<TextView>(R.id.nav_Name)
        val navEmail = headerView.findViewById<TextView>(R.id.nav_Email)
        val navLogin = headerView.findViewById<LinearLayout>(R.id.nav_login);
        val navNoLogin = headerView.findViewById<TextView>(R.id.nav_no_login);
        viewModel.userData.observe(this) { userModel ->
            Statics.ID = userModel.name
            Statics.Email = userModel.email

            navName.setText(userModel.name)
            navEmail.setText(userModel.email)
            Log.d("asdf",userModel.name)
            Log.d("asdf",userModel.email)

        }

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)
        val fragmentNumber = getIntent().getIntExtra("fragment_number", 0);

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment1 -> mainTitle.setText("메인")
                R.id.homeFragment2 -> mainTitle.setText("영화")
                R.id.homeFragment3 -> if (FireBaseAuthUtils.getUid() != "null") mainTitle.setText("마이페이지") else mainTitle.setText(
                    "로그인"
                )
            }

        }


        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Not used
            }

            override fun onDrawerOpened(drawerView: View) {
                // 네비게이션 뷰가 열릴 때마다 uid를 기반으로 네비게이션 아이템의 레이아웃 변경
                Log.d(TAG, "Statics.UID : " + Statics.UID)

                if (FireBaseAuthUtils.getUid() == "null") {
                    Log.d(TAG, "Statics.UID==null")
                    navLogInOutItem.title = "로그인"
                } else {
                    Log.d(TAG, "Statics.UID!=null")
                    navLogInOutItem.title = "로그아웃"
                }

                if (FireBaseAuthUtils.getUid() != "null") {
                    navLogin.visibility = View.VISIBLE
                    navNoLogin.visibility = View.GONE

                } else {
                    navLogin.visibility = View.GONE
                    navNoLogin.visibility = View.VISIBLE
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                // Not used
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Not used
            }
        })


        navigationView.setNavigationItemSelectedListener { menuItem ->
            val loginIntent = Intent(this, LoginActivity::class.java)
            val mapIntent = Intent(this, MapActivity::class.java)


            when (menuItem.itemId) {
                R.id.nav_map -> {
                    startActivity(mapIntent)
                }

                R.id.nav_review -> {
                    moveActivity(1)
                }

                R.id.nav_like -> {
                    moveActivity(2)
                }

                R.id.nav_setting -> {
                    moveActivity(3)
                }

                R.id.nav_login_logout -> {
                    if (FireBaseAuthUtils.getUid() == "null") {
                        startActivity(loginIntent)
                    } else {
                        FireBaseAuthUtils.signOut()
                        startActivity(loginIntent)
                    }

                }
            }
            val navLogInOutItem = navigationView.menu.findItem(R.id.nav_login_logout)


            drawerLayout.closeDrawer(GravityCompat.START) // 네비게이션 뷰 닫기
            false
        }


    }

    private fun moveActivity(type: Int) {
        val myReViewIntent = Intent(this, MyReviewActivity::class.java)
        val myMovieLikeIntent = Intent(this, MyLikeMovieActivity::class.java)
        val settingIntent = Intent(this, MyPageActivity::class.java)



        if (FireBaseAuthUtils.getUid() != "null") {
            if (type == 0) {

            } else if (type == 1) {
                startActivity(myReViewIntent)

            } else if (type == 2) {
                startActivity(myMovieLikeIntent)

            } else if (type == 3) {
                startActivity(settingIntent)

            }

        } else {
            Toast.makeText(this@MainActivity, "로그인 후 이용해 주세요", Toast.LENGTH_SHORT).show()

        }
        if (type == 4) {
            startActivity(settingIntent)
        }


    }

    /* override fun onNavigationItemSelected(item: MenuItem): Boolean {
         val navigationView = binding.navView
         val mapIntent = Intent(this, MapActivity::class.java)
         val loginIntent = Intent(this, MainActivity::class.java)


         when (item.itemId) {
             R.id.nav_home -> startActivity(mapIntent)

             R.id.nav_login_logout -> if (Const_Ho.LOGIN_CHECK) {
                 startActivity(mapIntent)
             } else {
                 logOutPopup()
             }
         }
         drawerLayout.closeDrawer(GravityCompat.START)
         return true
     }*/

    /*override fun onBackPressed() {
        finishPopup()
    }*/

    fun finishPopup() {
        val popup = CustomPopup(this, object : CustomPopup.PopupListener {
            override fun conFirmClick() {
                if (FireBaseAuthUtils.getUid() != null) {
                    FireBaseAuthUtils.signOut()
                }
                finishAffinity()
            }

            override fun cancelClick() {

            }
        }, "종료", "Hoho를 종료 하시겠습니까?", true)
        popup.setCancelable(false)
        popup.show()
    }

    fun logOutPopup() {
        val popup = CustomPopup(this, object : CustomPopup.PopupListener {
            override fun conFirmClick() {
                if (FireBaseAuthUtils.getUid() != null) {
                    FireBaseAuthUtils.signOut()
                }
            }

            override fun cancelClick() {

            }
        }, "로그아웃", "로그아웃 하시겠습니까?", true)
        popup.setCancelable(false)
        popup.show()
    }

    override fun onResume() {
        super.onResume()

    }
}


