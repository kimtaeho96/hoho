package com.hotta.hoho.view.join

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hotta.hoho.R
import com.hotta.hoho.databinding.FragmentPhoneFindBinding
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.utils.MLOG
import java.util.concurrent.TimeUnit

class PhoneFindFragment : Fragment() {
    private var _binding: FragmentPhoneFindBinding? = null
    private val binding get() = _binding!!
    var verificationId = ""
    val auth = Firebase.auth


    lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneFindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        binding.phoneSendBtn.setOnClickListener {
            val id = binding.phoneIdEdit.text.toString()
            val phone = binding.phoneEdit.text.toString()



            FireBaseRef.userFindInfo.orderByKey().equalTo(id)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(requireContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT)
                                .show()


                        } else {
                            val valueMap = dataSnapshot.value as Map<String, Any>
                            val phoneNumber = valueMap["hoho"] as? String
                            if (phone.equals(phoneNumber)) {
                                val callbacks = object :
                                    PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                        Log.d(
                                            "phoneSendBtn",
                                            "Verification completed with credential: ${credential.smsCode}"
                                        )
                                    }

                                    override fun onVerificationFailed(e: FirebaseException) {
                                        Log.e("phoneSendBtn", "Verification failed", e)
                                    }

                                    override fun onCodeSent(
                                        verificationId: String,
                                        token: PhoneAuthProvider.ForceResendingToken
                                    ) {
                                        this@PhoneFindFragment.verificationId = verificationId
                                        Log.d("phoneSendBtn", "Code sent: $verificationId")
                                        startCountDownTimer()
                                    }
                                }

                                val optionsCompat = PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber("+821012345678")
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(requireActivity())
                                    .setCallbacks(callbacks)
                                    .build()

                                PhoneAuthProvider.verifyPhoneNumber(optionsCompat)
                                auth.setLanguageCode("kr")

                            } else {
                                Toast.makeText(requireContext(), "틀린 번호 입니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }


                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("1231241", "값이 있다. ${databaseError}")

                    }
                })


        }


        // 인증 코드 입력 버튼 리스너 설정
        binding.phoneAccessBtn.setOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            binding.phoneAccessEdit.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(
                binding.phoneAccessEdit.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            val accessNumber = binding.phoneAccessEdit.text.toString()

            if (verificationId != null && accessNumber.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, accessNumber)
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(context, "인증 코드나 Verification ID가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.pwdChangeBtn.setOnClickListener {

            val newPwd = binding.newPwdEt.text.toString()
            val newCheckPwd = binding.newPwdCheckEt.text.toString()

            if (newPwd.isNotEmpty() && newCheckPwd.isNotEmpty()) {
                if (newPwd == newCheckPwd) {
                    auth.currentUser?.let {
                        it.updatePassword(newPwd)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "비밀번호 변경", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                                }


                                auth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                                    if (deleteTask.isSuccessful) {
                                        Toast.makeText(
                                            requireContext(),
                                            "인증 성공, 사용자 로그인 취소됨",
                                            Toast.LENGTH_SHORT
                                        ).show()



                                    } else {
                                        binding.timerTextView.text = "인증 실패"

                                        Toast.makeText(
                                            requireContext(),
                                            "사용자 삭제 실패",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }


                                }
                            }
                    }


                } else {
                    Toast.makeText(context, "비밀번호가 다르다", Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(context, "비밀번호를 입력", Toast.LENGTH_SHORT).show()

            }


        }


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    countDownTimer.cancel()
                    binding.timerTextView.text = "인증 성공"
                    binding.pwdChangeLl.visibility = View.VISIBLE

                } else {
                    Toast.makeText(requireContext(), "인증 실패", Toast.LENGTH_SHORT).show()
                    binding.timerTextView.text = "인증 실패"


                }
            }

    }

    private fun startCountDownTimer() {
        binding.phoneAccessNumberLl.visibility = View.VISIBLE

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTextView.text = "남은 시간: ${millisUntilFinished / 1000}초"
            }

            override fun onFinish() {
                binding.timerTextView.text = "시간 초과! 다시 시도해 주세요."
                binding.timerTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
        }

        countDownTimer.start()
    }
}