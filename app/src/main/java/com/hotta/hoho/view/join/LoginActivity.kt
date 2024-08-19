package com.hotta.hoho.view.join


import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.hotta.hoho.R
import com.hotta.hoho.Statics
import com.hotta.hoho.databinding.ActivityLoginBinding
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.view.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel: JoinViewModel by viewModels()

    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit
    private val TAG = "asfdasfdasdf"

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        KakaoSdk.init(this, "d2a3b5eae2741acf2477f03de06627ca")


        //폰트적용시 투명? 기본폰트 따로 적용
        binding.LoginPwdArea.setTypeface(Typeface.DEFAULT)

        binding.loginBtn.setOnClickListener {
            val email = binding.LoginEmailArea.text.toString()
            val pwd = binding.LoginPwdArea.text.toString()

            val pattern = Patterns.EMAIL_ADDRESS

            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()

            } else {
                if (pattern.matcher(email).matches()) {
                    viewModel.login(this, email, pwd)
                } else {
                    Toast.makeText(this, "이메일 형식이 틀렸습니다.", Toast.LENGTH_SHORT).show()
                }

            }
            viewModel.loginResult.observe(this, Observer {
                if (it) {
                    Statics.UID = FireBaseAuthUtils.getUid()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.forgetPwdBtn.setOnClickListener {
            val intent = Intent(this, ForgetPwdActivity::class.java)
            startActivity(intent)
        }


        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }


        firebaseAuth = FirebaseAuth.getInstance()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                Log.e(TAG, "resultCode : ${result.resultCode}")
                Log.e(TAG, "result : $result")
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        task.getResult(ApiException::class.java)?.let { account ->
                            val tokenId = account.idToken
                            if (tokenId != null && tokenId != "") {
                                val credential: AuthCredential =
                                    GoogleAuthProvider.getCredential(account.idToken, null)


                                firebaseAuth.signInWithCredential(credential)
                                    .addOnCompleteListener {
                                        if (firebaseAuth.currentUser != null) {
                                            val user: FirebaseUser = firebaseAuth.currentUser!!
                                            val email = user.email.toString()
                                            val name = user.displayName.toString()
                                            val type = "google"
                                            val uid = user.uid

                                            val userModel = UserModel(name, email, type)
                                            viewModel.userDataInsert(uid, userModel)
                                            Log.e(TAG, "email : $email")
                                            val googleSignInToken = account.idToken ?: ""
                                            if (googleSignInToken != "") {
                                                Log.e(TAG, "googleSignInToken : $googleSignInToken")
                                            } else {
                                                Log.e(TAG, "googleSignInToken이 null")
                                            }
                                        }
                                    }
                            }
                        } ?: throw Exception()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        binding.btnGoogleSignIn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id)).requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)
                val signInIntent: Intent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }

        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                        UserApiClient.instance.me { user, error ->
                            Log.d("kakao", "닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                            Log.d("kakao", "이메일: ${user?.kakaoAccount?.email}")
                            Log.d("kakao", "프로필: ${user?.kakaoAccount?.profile?.thumbnailImageUrl}")

                            Log.d("kakao", "토큰: ${token.accessToken}")


                            val email = user?.kakaoAccount?.email.toString()
                            val name = user?.kakaoAccount?.profile?.nickname.toString()
                            val type = "kakao"
                            val uid = token.accessToken.toString()
                            val firebaseUid = email.replace('.', '_')

                            val userModel = UserModel(name, email, type, uid)
                            viewModel.userDataInsert(firebaseUid, userModel)
                        }
                    }
                }
            }
        }
    }
}