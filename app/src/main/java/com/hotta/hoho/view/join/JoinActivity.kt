package com.hotta.hoho.view.join

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityJoinBinding
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.main.MainActivity

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    private val viewModel: JoinViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private var idFlag = false
    private var passwordFlag = false
    private var passwordCheckFlag = false
    private var nameFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameInputLayout.editText?.addTextChangedListener(nameListener)
        //  binding.emailInputLayout.editText?.addTextChangedListener(idListener)
        binding.emailCheckBtn.setOnClickListener {
            showDialog(binding.emailInputEditText.text.toString())
        }

//        binding.pwdInputEditText.setHintTextColor(ContextCompat.getColor(this, R.color.lavender))
        binding.pwdInputLayout.editText?.addTextChangedListener(passwordListener)
        binding.pwdInputEditText.hint = "숫자 6자리 이상 입력해 주세요"
        binding.pwdInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.pwdInputEditText.hint = ""
            } else {
                binding.pwdInputEditText.hint = "숫자 6자리 이상 입력해 주세요"
            }
        }

        binding.pwdCheckInputEditText.setHintTextColor(
            ContextCompat.getColor(
                this,
                R.color.lavender
            )
        )
        binding.pwdCheckInputLayout.editText?.addTextChangedListener(passwordCheckListener)
        binding.pwdCheckInputEditText.hint = "비밀번호 확인"
        binding.pwdCheckInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.pwdCheckInputEditText.hint = ""
            } else {
                binding.pwdCheckInputEditText.hint = "비밀번호 확인"
            }
        }



        binding.joinBtn.setOnClickListener {
            val name = binding.nameInputEditText.text.toString()
            val email = binding.emailInputEditText.text.toString()
            val pwd = binding.pwdInputEditText.text.toString()
            val type = "email"
            val userModel = UserModel(name, email, type)
            try {
                viewModel.join(this, email, pwd)

                viewModel.joinResult.observe(this, Observer {
                    if (it) {
                        //회원정보를 DB에 저장````````````````````````````````````````

                        viewModel.userDataInsert(FireBaseAuthUtils.getUid(), userModel)
                        FireBaseRef.emailCheck.push().setValue(email)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, "회원가입실패 재시도", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d("JoinActivity1", e.toString())
            }


        }


    }

    fun passwordRegex(password: String): Boolean {
        //  return password.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,16}$".toRegex())
        return password.matches("^[0-9]{6,}$".toRegex())
    }

    // 특수문자 존재 여부를 확인하는 메서드
    private fun hasSpecialCharacter(string: String): Boolean {
        for (i in string.indices) {
            if (!Character.isLetterOrDigit(string[i])) {
                return true
            }
        }
        return false
    }

    // 영문자 존재 여부를 확인하는 메서드
    private fun hasAlphabet(string: String): Boolean {
        for (i in string.indices) {
            if (Character.isAlphabetic(string[i].code)) {
                return true
            }
        }
        return false
    }

    // 위의 두 메서드를 포함하여 종합적으로 id 정규식을 확인하는 메서드
    fun idRegex(id: String): Boolean {
        if ((!hasSpecialCharacter(id)) and (hasAlphabet(id)) and (id.length >= 5)) {
            return true
        }
        return false
    }

    fun hasWhitespace(id: String): Boolean {
        return id.contains(" ")
    }

    private val nameListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.nameInputLayout.error = "이름을 입력해 주세요."
                        nameFlag = false
                    }


                    hasWhitespace(s.toString()) -> {
                        binding.nameInputLayout.error = "공백을 제거해 주세요"
                        nameFlag = false
                    }

                    else -> {
                        binding.nameInputLayout.error = null
                        nameFlag = true
                    }
                }
                flagCheck()
            }
        }
    }

    private val idListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.emailInputLayout.error = "이메일을 입력해 주세요."
                        idFlag = false
                    }

                    /*  !idRegex(s.toString()) -> {
                          binding.emailInputLayout.error = "이메일 양식이 맞지 않습니다"
                          idFlag = false
                      }
  */

                    hasWhitespace(s.toString()) -> {
                        binding.emailInputLayout.error = "공백을 제거해 주세요"
                        idFlag = false
                    }

                    else -> {
                        binding.emailInputLayout.error = null
                        idFlag = true
                    }
                }
                flagCheck()
            }
        }
    }


    private val passwordListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.pwdInputLayout.error = "비밀번호를 입력해주세요."
                        passwordFlag = false
                    }

                    !passwordRegex(s.toString()) -> {
                        binding.pwdInputLayout.error = "비밀번호 양식이 일치하지 않습니다."
                        passwordFlag = false
                    }

                    hasWhitespace(s.toString()) -> {
                        binding.pwdInputLayout.error = "공백을 제거해 주세요"
                        passwordFlag = false
                    }

                    else -> {
                        binding.pwdInputLayout.error = null
                        passwordFlag = true
                    }
                }
                flagCheck()
            }
        }
    }
    private val passwordCheckListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val pwd = binding.pwdInputEditText.text.toString()

            if (s != null) {
                when {
                    s.isEmpty() -> {
                        binding.pwdCheckInputLayout.error = "비밀번호를 입력해주세요."
                        passwordCheckFlag = false
                    }

                    pwd != s.toString() -> {
                        Log.d("pwd", pwd)
                        binding.pwdCheckInputLayout.error = "비밀번호가 다릅니다."
                        passwordCheckFlag = false
                    }

                    hasWhitespace(s.toString()) -> {
                        binding.pwdCheckInputLayout.error = "공백을 제거해 주세요"
                        idFlag = false
                    }

                    else -> {
                        binding.pwdCheckInputLayout.error = null
                        passwordCheckFlag = true
                    }
                }
                flagCheck()
            }
        }
    }

    fun flagCheck() {
        binding.joinBtn.isEnabled = idFlag && passwordFlag && passwordCheckFlag && nameFlag
    }

    private fun showDialog(email: String) {
        viewModel.emailCheck()

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.emailcheck, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("이메일 중복확인")

        val mAlertDialog = mBuilder.show()
        val emailArea = mDialogView.findViewById<TextInputEditText>(R.id.emailCheckArea)
        Log.d("email", email)
        if (email == "이메일을 입력해 주세요") {
            emailArea.setText("")
        } else {
            emailArea.setText(email)
        }
        val emailTv = mDialogView.findViewById<TextView>(R.id.emailCheckTv)
        val emailAreaLy = mDialogView.findViewById<TextInputLayout>(R.id.emailCheckAreaLayout)
        emailAreaLy.editText?.addTextChangedListener(idListener(mDialogView))

        val emailBtn = mDialogView.findViewById<Button>(R.id.emailCheckBtns)
        val emailPassBtn = mDialogView.findViewById<Button>(R.id.emailUseBtn)
        emailBtn.setOnClickListener {
            viewModel.emailCheckResult.observe(this, Observer {
                if (it.contains(emailArea.text.toString())) {
                    emailTv.setText("중복된 이메일 입니다.")
                } else {
                    if (idFlag) {
                        emailTv.setText("사용 가능한 이메일 입니다.")
                        binding.emailInputEditText.setText(emailArea.text.toString())
                        emailPassBtn.isEnabled = true
                    } else {
                        emailTv.setText("이메일 형식을 맞춰 주세요")
                    }
                }
            })


        }
        emailPassBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }


    }

    private fun idListener(mDialogView: View): TextWatcher {
        return object : TextWatcher {
            val emailAreaLy = mDialogView.findViewById<TextInputLayout>(R.id.emailCheckAreaLayout)
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    // mDialogView를 사용하여 레이아웃 요소에 접근
                    if (s.isEmpty()) {
                        emailAreaLy.error = "이메일을 입력해 주세요."
                        idFlag = false
                    }
                    // 원하는 동작 수행
                    else if (hasWhitespace(s.toString())) {
                        emailAreaLy.error = "공백을 제거해 주세요"
                        idFlag = false
                    } else {
                        emailAreaLy.error = null
                        idFlag = true
                    }

                    flagCheck()
                }
            }
        }
    }
}