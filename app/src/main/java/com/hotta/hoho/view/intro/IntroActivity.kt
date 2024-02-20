package com.hotta.hoho.view.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.hotta.hoho.Statics
import com.hotta.hoho.databinding.ActivitySplahBinding
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.view.main.MainActivity

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplahBinding
    private val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplahBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (FireBaseAuthUtils.getUid() != null) {
            Statics.UID = FireBaseAuthUtils.getUid()

        } else {
            Statics.UID = ""

        }

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, 3000)


    }
}