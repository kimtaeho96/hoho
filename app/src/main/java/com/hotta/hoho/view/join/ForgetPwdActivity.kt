package com.hotta.hoho.view.join

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityForgetPwdBinding
import com.hotta.hoho.databinding.ActivityJoinBinding
import com.hotta.hoho.view.adapter.ViewPagerAdapter

class ForgetPwdActivity : AppCompatActivity() {
    private val viewModel: JoinViewModel by viewModels()
    lateinit var binding: ActivityForgetPwdBinding
    private  val NUM_PAGES = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tabTextList = arrayListOf("이메일로 찾기", "휴대폰으로 찾기")

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val adapter = ScreenSlidePagerAdapter(this@ForgetPwdActivity)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()

    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES // 페이지 수 리턴

        override fun createFragment(position: Int): Fragment {
            return when(position){ // 페이지 포지션에 따라 그에 맞는 프래그먼트를 보여줌
                0 -> EmailFindFragment()
                else -> PhoneFindFragment()
            }
        }
    }

}