package com.hotta.hoho.view.mymovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotta.hoho.databinding.ActivityMyLikeMovieBinding
import com.hotta.hoho.network.model.DetailMovieResponse
import com.hotta.hoho.view.adapter.LikeMovieAdapter
import com.hotta.hoho.view.adapter.MyReviewAdapter

class MyLikeMovieActivity : AppCompatActivity() {
    private val viewModel: MyMovieLikeViewModel by viewModels()
    lateinit var binding: ActivityMyLikeMovieBinding

    lateinit var myMovieDetail: ArrayList<DetailMovieResponse>
    lateinit var adapter: LikeMovieAdapter
    var likeMovieSize=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLikeMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getMyLikeMovieId()

        myMovieDetail = ArrayList()

        viewModel.myMovieLikeIds.observe(this) {
            myMovieDetail.clear()
             likeMovieSize = it.size

            for (item in it) {
                Log.d("test1234-item", item.toString())

                viewModel.getDetailMovie(Integer.parseInt(item))
            }


        }
        viewModel.detailMovieResult.observe(this) {
            myMovieDetail.add(it)

            Log.d("test1234",it.title.toString())
            if (likeMovieSize == myMovieDetail.size) {
                Log.d("test1234-size1", myMovieDetail.size.toString())
                Log.d("test1234-size2", likeMovieSize.toString())

                adapter = LikeMovieAdapter(this, myMovieDetail)
                binding.myLikeMovieRv.adapter = adapter
                binding.myLikeMovieRv.layoutManager = GridLayoutManager(this, 3)


            }

        }

    }

    override fun onResume() {
        super.onResume()
        myMovieDetail = ArrayList()

    }
}