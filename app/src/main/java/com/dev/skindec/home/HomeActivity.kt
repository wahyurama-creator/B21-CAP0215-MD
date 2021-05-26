package com.dev.skindec.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dev.skindec.ResultActivity
import com.dev.skindec.databinding.ActivityHomeBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var filePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            onBackPressed()
        }

        binding.ivImage.setOnClickListener {
            ImagePicker.with(this)
                .start()
        }

        binding.btnUpload.setOnClickListener {
            val name = binding.etName.text.toString()
            val age = binding.etAge.text.toString()
            val sex = binding.etGender.text.toString()

            val userObject = JsonObject()
            userObject.addProperty("age", age)
            userObject.addProperty("name", name)
            userObject.addProperty("sex", sex)

//            homeViewModel.userRegister(userObject)

            startActivity(Intent(this, ResultActivity::class.java))
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                filePath = data?.data!!
                Glide.with(this)
                    .load(filePath)
                    .circleCrop()
                    .into(binding.ivImage)
            }
            ImagePicker.RESULT_ERROR -> {
                Snackbar.make(
                    binding.btnUpload,
                    ImagePicker.getError(data),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
            else -> {
                Snackbar.make(
                    binding.btnUpload,
                    "Task cancelled",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}