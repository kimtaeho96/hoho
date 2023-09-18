package com.hotta.hoho.view.mypage

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ActivityJoinBinding
import com.hotta.hoho.databinding.ActivityMyPageBinding
import com.hotta.hoho.utils.FireBaseAuthUtils
import java.io.ByteArrayOutputStream

class MyPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val getName = intent.getStringExtra("name").toString()
        val getEmail = intent.getStringExtra("email").toString()

        binding.myPageEmail.setText(getEmail)
        binding.myPageName.setText(getName)
        binding.myEmail.setText(getEmail)
        binding.myId.setText(getEmail)

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                binding.profileImg.setImageURI(uri)
                imgUpload()
            }
        )

        binding.profileImg.setOnClickListener {
            getAction.launch("image/*")
        }

        binding.profileNameChgBtn.setOnClickListener {

            showDialog()
        }

        binding.pwdChangeBtn.setOnClickListener {
            binding.pwdLayout1.visibility = View.GONE
            binding.pwdLayout2.visibility = View.VISIBLE
        }

        binding.pwdSave.setOnClickListener {

        }

        binding.pwdCancel.setOnClickListener {
            binding.pwdLayout1.visibility = View.VISIBLE
            binding.pwdLayout2.visibility = View.GONE
        }

        binding.brokebtn.setOnClickListener {


        }


    }

    fun imgUpload() {
        var storage = Firebase.storage
        val storageRef = storage.reference

        val mountainsRef = storageRef.child("profile").child(FireBaseAuthUtils.getUid() + ".png")

        binding.profileImg.isDrawingCacheEnabled = true
        binding.profileImg.buildDrawingCache()
        val bitmap = (binding.profileImg.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.name_change, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("이름 변경")

        val mAlertDialog = mBuilder.show()
        val nameArea = mDialogView.findViewById<TextInputEditText>(R.id.nameChangeArea)
        nameArea.setText(binding.myPageName.text)

        val changeBtn = mDialogView.findViewById<Button>(R.id.changeBtn)
        val cancelBtn = mDialogView.findViewById<Button>(R.id.cancelBtn)

        changeBtn.setOnClickListener {
            val name = nameArea.text

            /*   Firebase.auth.currentUser!!.updatePassword()
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {

                       }
                   }
   */

            binding.myPageName.setText(name)
            mAlertDialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }


    }
}