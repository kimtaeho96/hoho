package com.hotta.hoho.view.detail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityMovieDetailBinding
import com.hotta.hoho.network.model.ProductionCompany
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.adapter.CreditMovieAdapter
import com.hotta.hoho.view.adapter.ReviewMovieAdapter
import com.hotta.hoho.view.main.MainViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class MovieDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val viewModel: MainViewModel by viewModels()
    private val viewModel2: DetailViewModel by viewModels()

    lateinit var binding: ActivityMovieDetailBinding

    lateinit var getId: String
    val items = mutableListOf<ReviewModel>()
    lateinit var creditMovieAdapter: CreditMovieAdapter
    var genreList: ArrayList<String>? = null

    var allReviewKeyList = mutableListOf<String>()
    var goodReviewKeyList = mutableListOf<String>()
    var badReviewKeyList = mutableListOf<String>()

    var reviewLikeList = mutableListOf<String>()
    private var dialogCheck: Int? = 0

    lateinit var reviewMovieAdapter: ReviewMovieAdapter

    private lateinit var drawerLayout: DrawerLayout
    var allList = mutableListOf<ReviewModel>()
    var goodList = mutableListOf<ReviewModel>()
    var badList = mutableListOf<ReviewModel>()

    private var isLatestOrder = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val toolbar = binding.toolbar
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

        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navgationView.setCheckedItem(R.id.nav_home)
        }*/



        getId = intent.getStringExtra("id").toString()
        val reviewCount = binding.reviewCount



        Log.d("MovieDetailActivitys", getId.toString())

        viewModel.getDetailMovie(Integer.parseInt(getId))


        viewModel.detailMovieResult.observe(this, Observer {

            Log.d("MovieDetailActivitys", it.toString())
            genreList = ArrayList()
            for (item in it) {
                binding.title.text = item.title
                binding.story.text = item.overview

                for (gener in item.genres) {
                    Log.d("gener", gener.name.toString())
                    genreList?.add(gener.name)
                }
                Log.d("gener", genreList.toString())

                Glide.with(baseContext)
                    .load("https://image.tmdb.org/t/p/w342${item.backdrop_path}")
                    /* .transform(CenterCrop())*/
                    .centerCrop()
                    .into(binding.imageView2)

                Glide.with(baseContext)
                    .load("https://image.tmdb.org/t/p/w342${item.poster_path}")
                    /* .transform(CenterCrop())*/
                    .fitCenter()
                    .into(binding.imageView3)

                binding.enTitle.text = item.original_title
                // binding.bigTitle.text = item.title
                toolbar.title = item.title
                val genres = genreList!!.joinToString(", ")
                binding.genres.text = genres



                binding.runtime.text = item.runtime.toString()
                binding.releaseDate.text = item.release_date
                binding.voteAverage1.rating = ((item.vote_average) / 2).toFloat()
                binding.voteAverage2.text = String.format("%.1f", item.vote_average)


                val company: ArrayList<ProductionCompany> = ArrayList()
                for (items in item.production_companies) {
                    val productionCompany = ProductionCompany(
                        items.id,
                        items.logo_path,
                        items.name,
                        items.origin_country
                    )
                    company.add(productionCompany)
                }

                binding.productionCompanies.text = company[0].name
                binding.country.text = company[0].origin_country

            }


        })
        viewModel.getVideoMovie(Integer.parseInt(getId))
        viewModel.videoMovieResult.observe(this, Observer {
            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = it
                    Log.d("MoviedetailVide", it)
                    youTubePlayer.cueVideo(videoId, 0f)

                }
            })
        })

        viewModel.getCreditsMovie(Integer.parseInt(getId))
        viewModel.creditMovieResult.observe(this, Observer {
            val creditMovieAdapter = CreditMovieAdapter(this, it)
            binding.creditRV.adapter = creditMovieAdapter
            binding.creditRV.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL, false
            )
        })
        var check: Boolean = true


        binding.reviewBtn.setOnClickListener {

            /*if (FireBaseAuthUtils.getUid() != "null") {
                if (check) {
                     val intent = Intent(this, ReviewActivity::class.java)
                     intent.putExtra("id", getId)
                     startActivity(intent)
                } else {
                     Toast.makeText(this, "이미 리뷰를 작성하였습니다.", Toast.LENGTH_SHORT).show()

                }
                val intent = Intent(this, ReviewActivity::class.java)
                intent.putExtra("id", getId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "로그인후 이용할수있습니다.", Toast.LENGTH_SHORT).show()
            }*/

            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("id", getId)
            startActivity(intent)
        }

        binding.allReview.setOnClickListener {
            showDialog()

        }

        viewModel2.selectReviewLikeData()
        viewModel2.reviewLikeData.observe(this, Observer {
            allList = ArrayList()
            goodList = ArrayList()
            badList = ArrayList()
            //좋아용한 데이터를 가져온다.
            reviewLikeList = it as MutableList<String>


            //작성된 리뷰를 가져온다.
            viewModel2.selectReviewData(getId)

            viewModel2.reviewData.observe(this, Observer {

                reviewCount.setText(it.size.toString())

                allList.clear()
                goodList.clear()
                badList.clear()

                for (item in it) {
                    if (item.userid == FireBaseAuthUtils.getUid()) {
                        check = false
                    }

                    //
                    allReviewKeyList?.add(item.userid)


                    allList.add(item)
                    if (item.goodBad == "good") {
                        goodList.add(item)
                        goodReviewKeyList.add(item.userid)
                    } else if (item.goodBad == "bad") {
                        badList.add(item)
                        badReviewKeyList.add(item.userid)
                    }

                }


                if (dialogCheck == 0) {
                    reviewCount.setText(it.size.toString())
                    reviewMovieAdapter =
                        ReviewMovieAdapter(this, allList, getId, allReviewKeyList, reviewLikeList)
                }   //좋아요
                else if (dialogCheck == 1) {
                    reviewCount.setText(goodList.size.toString())
                    reviewMovieAdapter =
                        ReviewMovieAdapter(this, goodList, getId, goodReviewKeyList, reviewLikeList)
                } else {
                    reviewCount.setText(badList.size.toString())
                    reviewMovieAdapter =
                        ReviewMovieAdapter(this, badList, getId, badReviewKeyList, reviewLikeList)
                }

                binding.userRv.adapter = reviewMovieAdapter
                binding.userRv.layoutManager = LinearLayoutManager(this)


                reviewMovieAdapter.itemClick = object : ReviewMovieAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        val correction = view.findViewById<TextView>(R.id.correction)
                        val delete = view.findViewById<TextView>(R.id.deleteTv)

                        correction.setOnClickListener {
                            Toast.makeText(baseContext, "수정 클릭", Toast.LENGTH_SHORT).show()
                            val intent = Intent(baseContext, ReviewActivity::class.java)
                            intent.putExtra("수정", getId)
                            startActivity(intent)


                        }
                        delete.setOnClickListener {
                            Toast.makeText(baseContext, "삭제 클릭.", Toast.LENGTH_SHORT).show()

                            FireBaseRef.movieReview.child(getId)
                                .child(FireBaseAuthUtils.getUid())
                                .removeValue()

                            var storage = Firebase.storage
                            val storageRef = storage.reference
                            val mountainsRef =
                                storageRef.child(getId)
                                    .child(FireBaseAuthUtils.getUid() + ".png")
                            mountainsRef.delete().addOnSuccessListener {
                                check = true
                            }.addOnFailureListener {
                            }
                        }
                    }
                }


            })

        })
        binding.recentReivew.setOnClickListener {

//좋아요순으로 정렬
            if (isLatestOrder) {
                binding.recentReivew.setText("좋아요")
                //전체보기 좋아요순
                if (dialogCheck == 0) {
                    val sortedList = allList.sortedByDescending { it.count }
                    reviewMovieAdapter =
                        ReviewMovieAdapter(
                            this,
                            sortedList,
                            getId,
                            allReviewKeyList,
                            reviewLikeList
                        )
                    //좋았어요 좋아요순
                } else if (dialogCheck == 1) {
                    val sortedList = goodList.sortedByDescending { it.count }
                    reviewMovieAdapter =
                        ReviewMovieAdapter(
                            this,
                            sortedList,
                            getId,
                            goodReviewKeyList,
                            reviewLikeList
                        )


                    //별로에요 좋아요순
                } else {
                    val sortedList = badList.sortedByDescending { it.count }
                    reviewMovieAdapter =
                        ReviewMovieAdapter(
                            this,
                            sortedList,
                            getId,
                            badReviewKeyList,
                            reviewLikeList
                        )
                }
                isLatestOrder = false
                binding.userRv.adapter = reviewMovieAdapter
                binding.userRv.layoutManager = LinearLayoutManager(this)
            } else {
                //최신순으로 정렬
                isLatestOrder = true
                binding.recentReivew.setText("최신순")
                viewModel2.selectReviewData(getId)
            }


        }


    }


    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)

        val dialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()

        val allReview = dialog.findViewById<LinearLayout>(R.id.allReview)
        val goodReview = dialog.findViewById<LinearLayout>(R.id.goodReview)
        val badReview = dialog.findViewById<LinearLayout>(R.id.badReview)

        val allCheck = dialog.findViewById<ImageView>(R.id.allCheckImg)
        val goodCheck = dialog.findViewById<ImageView>(R.id.goodCheckImg)
        val badCheck = dialog.findViewById<ImageView>(R.id.badCheckImg)

        if (dialogCheck == 0) {
            allCheck?.visibility = View.VISIBLE
            goodCheck?.visibility = View.INVISIBLE
            badCheck?.visibility = View.INVISIBLE
        } else if (dialogCheck == 1) {
            allCheck?.visibility = View.INVISIBLE
            goodCheck?.visibility = View.VISIBLE
            badCheck?.visibility = View.INVISIBLE
        } else {
            allCheck?.visibility = View.INVISIBLE
            goodCheck?.visibility = View.INVISIBLE
            badCheck?.visibility = View.VISIBLE

        }


        allReview?.setOnClickListener {
            Toast.makeText(this, "전체보기", Toast.LENGTH_SHORT).show()
            allCheck?.visibility = View.VISIBLE
            goodCheck?.visibility = View.INVISIBLE
            badCheck?.visibility = View.INVISIBLE

            binding.allReview.setText("전체보기")
            dialogCheck = 0
            viewModel2.selectReviewData(getId)
            dialog.dismiss()
        }

        goodReview?.setOnClickListener {
            Toast.makeText(this, "좋았어요", Toast.LENGTH_SHORT).show()
            allCheck?.visibility = View.INVISIBLE
            goodCheck?.visibility = View.VISIBLE
            badCheck?.visibility = View.INVISIBLE

            binding.allReview.setText("좋았어요")
            dialogCheck = 1
            viewModel2.selectReviewData(getId)
            dialog.dismiss()

        }

        badReview?.setOnClickListener {
            Toast.makeText(this, "별로에요", Toast.LENGTH_SHORT).show()
            allCheck?.visibility = View.INVISIBLE
            goodCheck?.visibility = View.INVISIBLE
            badCheck?.visibility = View.VISIBLE

            binding.allReview.setText("별로에요")

            dialogCheck = 2
            viewModel2.selectReviewData(getId)

            dialog.dismiss()

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}