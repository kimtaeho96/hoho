package com.hotta.hoho.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hotta.hoho.Statics
import com.hotta.hoho.databinding.FragmentHome3Binding
import com.hotta.hoho.view.join.JoinActivity
import com.hotta.hoho.view.join.LoginActivity
import com.hotta.hoho.view.map.MapActivity
import com.hotta.hoho.view.mypage.MyPageActivity
import com.hotta.hoho.view.myreview.MyReviewActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient

import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.hotta.hoho.R
import com.hotta.hoho.view.adapter.ViewPagerAdapter
import com.hotta.hoho.view.main.dummy.dummyAdvData
import com.hotta.hoho.view.mymovie.MyLikeMovieActivity

class HomeFragment3 : Fragment() {
    private var _binding: FragmentHome3Binding? = null

    private val viewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
    private val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        KakaoSdk.init(requireContext(), "d2a3b5eae2741acf2477f03de06627ca")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHome3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = auth.currentUser?.uid.toString()
        Log.d("HomeFragment3_1", uid)


        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                if (uid == "null") {
                    binding.LoginN.visibility = View.VISIBLE
                } else {
                    binding.LoginN.visibility = View.GONE
                    binding.viewPagerIdol.visibility=View.GONE
                    binding.LoginY.visibility = View.VISIBLE

                }
            } else if (tokenInfo != null) {

                binding.LoginN.visibility = View.GONE
                binding.LoginY.visibility = View.VISIBLE
            }
        }
        binding.profileName.setText(Statics.ID)
        binding.profileEmail.setText(Statics.Email)

        binding.logout.setOnClickListener {
            //로그아웃
            auth.signOut()

            //구글 로그아웃
            val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val client = GoogleSignIn.getClient(requireContext(), opt)
            client.signOut()

            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.d("카카오", "카카오 로그아웃 실패")
                } else {
                    Log.d("카카오", "카카오 로그아웃 성공!")
                }
            }
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.joinBtn.setOnClickListener {
            val intent = Intent(requireContext(), JoinActivity::class.java)
            startActivity(intent)
        }
        binding.profileBtn.setOnClickListener {
            val intent = Intent(requireContext(), MyPageActivity::class.java)
            intent.putExtra("name", Statics.ID)
            intent.putExtra("email", Statics.Email)
            startActivity(intent)
        }
        binding.homeFragment3Setting.setOnClickListener {
            val intent = Intent(requireContext(), MyPageActivity::class.java)
            intent.putExtra("name", Statics.ID)
            intent.putExtra("email", Statics.Email)
            startActivity(intent)
        }




        binding.closeMovie.setOnClickListener {
            val mapIntent = Intent(requireContext(), MapActivity::class.java)
            startActivity(mapIntent)
        }

        binding.homeFragment3Like.setOnClickListener {
            val movieLikeIntent = Intent(requireContext(), MyLikeMovieActivity::class.java)
            startActivity(movieLikeIntent)
        }

        binding.homeFragment3Write.setOnClickListener {
            val myReviewIntent = Intent(requireContext(), MyReviewActivity::class.java)
            startActivity(myReviewIntent)
        }

        binding.viewPagerIdol.adapter = ViewPagerAdapter(requireContext(),getIdolList())
        binding.viewPagerIdol.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerIdol.setPageTransformer(ZoomOutPageTransformer())


    }

    private fun getIdolList(): ArrayList<dummyAdvData> {
        return arrayListOf(
            dummyAdvData("http://www.cgv.co.kr/culture-event/event/detailViewUnited.aspx?seq=41251&menu=001", R.drawable.adv1),
            dummyAdvData("http://www.cgv.co.kr/culture-event/event/detailViewUnited.aspx?seq=41264&menu=001", R.drawable.adv2),
            dummyAdvData("http://www.cgv.co.kr/culture-event/event/detailViewUnited.aspx?seq=41376&menu=001", R.drawable.adv3)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(com.hotta.hoho.R.id.action_homeFragment3_to_homeFragment2)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    /* 공식문서에 있는 코드 긁어온거임 */
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}
