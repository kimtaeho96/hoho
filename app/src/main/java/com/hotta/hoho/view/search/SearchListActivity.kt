package com.hotta.hoho.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.fragment.app.Fragment

import com.hotta.hoho.databinding.ActivitySearchListBinding
import com.hotta.hoho.view.adapter.MovieSearchAdapter

class SearchListActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels()
    lateinit var binding: ActivitySearchListBinding

    lateinit var movieSearchAdapter: MovieSearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val searchText = intent.getStringExtra("search")

        binding.searchTv.setText(searchText)

        if (searchText != null) {
            viewModel.getSearchMovie(searchText)
            viewModel.movieSearch.observe(this) {

                movieSearchAdapter = MovieSearchAdapter(this, it)

                binding.searchMovieRv.adapter = movieSearchAdapter
                binding.searchMovieRv.layoutManager = StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.HORIZONTAL
                )

                binding.searchNumber.setText("(${it.size})")

            }
        } else {

        }

    }
}