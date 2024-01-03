package com.hotta.hoho.view.credit

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.hotta.hoho.databinding.ActivityCreditBinding
import com.hotta.hoho.datamodel.PeopleImgResult
import com.hotta.hoho.utils.MLOG
import com.hotta.hoho.view.adapter.PeopleImgAdapter
import com.hotta.hoho.view.adapter.PeopleMovieAdapter

class CreditActivity : AppCompatActivity() {
    private val TAG = "!!@@" + CreditActivity::class.java.simpleName
    private val viewModel: CreditViewModel by viewModels()
    lateinit var binding: ActivityCreditBinding
    lateinit var peopleImgAdapter: PeopleImgAdapter
    lateinit var imgList: ArrayList<PeopleImgResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0);

        MLOG.d(TAG, "id : $id");

        viewModel.getPeopleDetail(id)
        viewModel.peopleDetail.observe(this) {

            Glide.with(baseContext)
                .load("https://image.tmdb.org/t/p/w342${it.profilePath}")
                .fitCenter()
                .into(binding.detailProfileIv)

            binding.creditNameTv.text = it.name
            binding.creditBirthTv.text = it.birthday
            binding.creditBornTv.text = it.placeOfBirth
            binding.creditBiographyTv.text = it.biography


        }
        viewModel.getPeopleMovie(id)
        viewModel.peopleMovie.observe(this) {
            val peopleMovieAdapter = PeopleMovieAdapter(this, it, this)
            binding.creditRv.adapter = peopleMovieAdapter
            binding.creditRv.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL,
                false
            )
        }

        viewModel.getPeopleImg(id)
        viewModel.peopleImg.observe(this) {
            peopleImgAdapter = PeopleImgAdapter(this, it)

            binding.creditImgRv.adapter = peopleImgAdapter
            binding.creditImgRv.layoutManager = StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
            )

            imgList = ArrayList()

            for (data in it) {
                imgList.add(data)
            }

            peopleImgAdapter.itemClick = object : PeopleImgAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {


                    binding.slideLayout.visibility = View.VISIBLE

                    val imgView = binding.slidePeopleImg


                    Glide.with(imgView)
                        .load("https://image.tmdb.org/t/p/w342${imgList.get(position).file_path}")
                        .fitCenter()
                        .centerCrop()
                        .into(imgView)

                    binding.slidePeopleImgCancel.setOnClickListener {
                        binding.slideLayout.visibility = View.GONE
                    }

                }


            }

        }


    }



}


