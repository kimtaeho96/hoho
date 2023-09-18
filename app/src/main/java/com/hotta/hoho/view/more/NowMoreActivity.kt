package com.hotta.hoho.view.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityMainBinding
import com.hotta.hoho.databinding.ActivityNowMoreBinding
import com.hotta.hoho.view.more.paging.LoadMoreAdapter
import com.hotta.hoho.view.more.paging.MovieAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

class NowMoreActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityNowMoreBinding
    lateinit var moviesAdapter: MovieAdapter
    private lateinit var drawerLayout: DrawerLayout

    private val viewModel: MoreViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNowMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        drawerLayout = binding.drawerLayout

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.drawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END);
        }


        val type = intent.getStringExtra("type").toString()

        Log.d("NowMoreActivity1", type)

        val headerView: View = navigationView.getHeaderView(0)
        val navName = headerView.findViewById<TextView>(R.id.nav_Name)
        navName.setText("이건가용")

        binding.rv.apply {
            layoutManager = GridLayoutManager(baseContext, 2)
            moviesAdapter = MovieAdapter(baseContext)
            adapter = moviesAdapter
        }
        viewModel.type(type)

        if (type.equals("now")) {
            lifecycleScope.launch {
                viewModel.nowMovieList.collect { pagingData ->
                    moviesAdapter.submitData(pagingData)
                }
            }
        } else if (type.equals("pop")) {
            lifecycleScope.launch {
                viewModel.popMovieList.collect { pagingData ->
                    moviesAdapter.submitData(pagingData)
                }
            }
        } else if (type.equals("top")) {
            lifecycleScope.launch {
                viewModel.topMovieList.collect { pagingData ->
                    moviesAdapter.submitData(pagingData)
                }
            }
        } else {
            lifecycleScope.launch {
                viewModel.upMovieList.collect { pagingData ->
                    moviesAdapter.submitData(pagingData)
                }
            }

            lifecycleScope.launch {
                moviesAdapter.loadStateFlow.collect {
                    val state = it.refresh
                    binding.prgBarMovies.isVisible = state is LoadState.Loading
                }
            }

            binding.rv.adapter = moviesAdapter.withLoadStateFooter(LoadMoreAdapter {
                moviesAdapter.retry()
            })
        }
    }

        override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START) // 네비게이션 드로어가 열려있는 경우 닫기
            } else {
                super.onBackPressed() // 네비게이션 드로어가 닫혀있는 경우 기본 뒤로 가기 동작 실행
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item?.itemId) {
                android.R.id.home -> {
                    finish()
                    return true
                }

                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item?.itemId) {
                android.R.id.home -> {
                    finish()
                    return true
                }

                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
        }
    }
