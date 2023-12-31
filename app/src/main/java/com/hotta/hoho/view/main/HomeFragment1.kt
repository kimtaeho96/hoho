package com.hotta.hoho.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.hotta.hoho.databinding.FragmentHome1Binding
import com.hotta.hoho.view.adapter.DayMovieAdapter
import com.hotta.hoho.view.detail.MovieDetailActivity
import com.hotta.hoho.view.search.SearchActivity


class HomeFragment1 : Fragment() {

    private var _binding: FragmentHome1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHome1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchLayout.setOnClickListener {

            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)

        }

        viewModel.getWeather()

        viewModel.weatherResult.observe(viewLifecycleOwner, Observer {

            for (item in it) {
                if (item.category.equals("SKY")) {
                    Log.d("weather", item.category)

                    if (item.fcstValue == "1") {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "맑음")

                    } else if (item.fcstValue == "3") {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "구름많음")
                        binding.weatherTv.setText("구름많음")
                        viewModel.getMoviesByGenre("27")


                    } else {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "흐림")
                        binding.weatherTv.setText("흐림")

                    }
                } else if (item.category.equals("PTY")) {
                    Log.d("weather", item.category)

                    if (item.fcstValue == "0") {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "맑음")
                    } else if (item.fcstValue == "1") {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "비")
                        binding.weatherTv.setText("비")
                        viewModel.getMoviesByGenre("27")

                    } else if (item.fcstValue == "2") {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "비/눈")
                        binding.weatherTv.setText("비/눈")
                    } else if (item.fcstValue == "3") {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "눈")
                        binding.weatherTv.setText("눈")
                    } else {
                        Log.d("weather", item.fcstValue)
                        Log.d("weather", "소나기")
                        binding.weatherTv.setText("소나기")
                    }
                }
            }

        })
        viewModel.genereMovieResult.observe(viewLifecycleOwner, Observer {


            val movieId = it.id // 영화의 ID 가져오기
            val posterPath = it.poster_path // 영화의 포스터 경로 가져오기
            Log.d("RandomMovie", "Movie ID: $movieId, Poster Path: $posterPath")
            Glide.with(view)
                .load("https://image.tmdb.org/t/p/w342${posterPath}")
                .transform(CenterCrop())
                .fitCenter()
                .into(binding.randomMoviePoster)

            binding.randomMoviePoster.setOnClickListener {
                val intent = Intent(requireContext(), MovieDetailActivity::class.java)
                intent.putExtra("id", movieId.toString())
                startActivity(intent)

            }

        })

        /*viewModel.getDayMovie()
        viewModel.dayMovieResult.observe(viewLifecycleOwner, Observer {
            val dayMovieAdapter = DayMovieAdapter(requireContext(), it)
            binding.movieRv1.adapter = dayMovieAdapter
            binding.movieRv1.layoutManager = LinearLayoutManager(requireContext())

        })*/


    }

}