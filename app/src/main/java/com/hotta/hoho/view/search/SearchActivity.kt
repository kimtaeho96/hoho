package com.hotta.hoho.view.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotta.hoho.App
import com.hotta.hoho.Const_Ho
import com.hotta.hoho.databinding.ActivitySearchBinding
import com.hotta.hoho.db.entity.SearchListEntity
import com.hotta.hoho.view.adapter.RecentlySearchAdapter
import com.hotta.hoho.view.main.MainActivity


class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    val context = App.context()

    private val searchList = ArrayList<SearchListEntity>()

    lateinit var recentlyAdapter: RecentlySearchAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Const_Ho.AUTO_SAVE) {
            binding.autoSaveLl.visibility = View.VISIBLE
            binding.autoSaveRv.visibility = View.VISIBLE
            binding.autoSaveOnOff.setText("자동저장 끄기")
            Const_Ho.AUTO_SAVE = true
        } else {
            binding.autoSaveLl.visibility = View.GONE
            binding.autoSaveRv.visibility = View.GONE
            binding.autoSaveOnOff.setText("자동저장 켜기")

        }

        binding.autoSaveOnOff.setOnClickListener {
            if (Const_Ho.AUTO_SAVE) {
                binding.autoSaveLl.visibility = View.GONE
                binding.autoSaveRv.visibility = View.GONE
                binding.test1.visibility = View.VISIBLE
                binding.autoSaveOnOff.setText("자동저장 켜기")
                Const_Ho.AUTO_SAVE = false
            } else {
                binding.autoSaveLl.visibility = View.VISIBLE
                binding.autoSaveRv.visibility = View.VISIBLE
                binding.autoSaveOnOff.setText("자동저장 끄기")
                binding.test1.visibility = View.GONE
                Const_Ho.AUTO_SAVE = true
            }


        }




        viewModel.getAllSearchData()
        viewModel.searchDataList.observe(this) {
            Log.d("searchDataList", " item.toString()")

            searchList.clear()

            for (item in it) {
                searchList.add(item)
                Log.d("item", item.toString())
            }
            searchList.reverse()

            if (!searchList.isEmpty()) {
                recentlyAdapter = RecentlySearchAdapter(context, searchList,viewModel)
                binding.autoSaveRv.adapter = recentlyAdapter
                binding.autoSaveRv.layoutManager = LinearLayoutManager(this)
            }


            binding.autoSaveAllDelete.setOnClickListener {
                viewModel.getAllDeleteData()
                recentlyAdapter.notifyDataSetChanged()
            }


        }

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.searchArea.setText("아저씨")
        binding.searchBtn.setOnClickListener {
            val searchText = binding.searchArea.text.toString()

            if (!searchText!!.isEmpty()) {
                if (Const_Ho.AUTO_SAVE) {
                    viewModel.saveSearchData(searchText)
                }
                val intent = Intent(context, SearchListActivity::class.java)
                intent.putExtra("search", searchText)
                startActivity(intent)
                binding.searchArea.setText("")

            } else {
                Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
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