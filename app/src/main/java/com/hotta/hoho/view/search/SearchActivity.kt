package com.hotta.hoho.view.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hotta.hoho.databinding.ActivitySearchBinding
import com.hotta.hoho.view.main.MainActivity


class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener {


        }

        binding.searchBtn.setOnClickListener {
            var searchText = binding.searchArea.text
            searchText
        }

        binding.searchArea.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            val intent = Intent(this, MainActivity::class.java)
            val searchTest = binding.searchArea.text.toString()
            intent.putExtra("search", searchTest)

            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> startActivity(intent)
            }
            false
        })


    }
}