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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hotta.hoho.databinding.FragmentHome3Binding
import com.hotta.hoho.view.join.JoinActivity
import com.hotta.hoho.view.join.LoginActivity
import com.hotta.hoho.view.mypage.MyPageActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient


class HomeFragment3 : Fragment() {
    private var _binding: FragmentHome3Binding? = null

    private val viewModel: MainViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


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

        viewModel.getUserInfo(uid)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                if (uid == "null") {
                    binding.LoginN.visibility = View.VISIBLE
                } else {
                    binding.LoginN.visibility = View.GONE
                    binding.LoginY.visibility = View.VISIBLE

                }
            } else if (tokenInfo != null) {
                binding.LoginN.visibility = View.GONE
                binding.LoginY.visibility = View.VISIBLE
            }
        }

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



        viewModel.userData.observe(viewLifecycleOwner, Observer { userModel ->
            binding.profileEmail.setText(userModel.email)
            binding.profileName.setText(userModel.name)

            Log.d("main3", userModel.email)


            binding.profileBtn.setOnClickListener {
                val intent = Intent(requireContext(), MyPageActivity::class.java)
                intent.putExtra("name", userModel.name)
                intent.putExtra("email", userModel.email)
                startActivity(intent)

            }
        })

    }



}