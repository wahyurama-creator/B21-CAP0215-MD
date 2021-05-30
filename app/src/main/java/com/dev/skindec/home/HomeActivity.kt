package com.dev.skindec.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dev.skindec.R
import com.dev.skindec.ResultActivity
import com.dev.skindec.core.data.source.remote.network.ApiConfig
import com.dev.skindec.core.data.source.remote.response.UserResponse
import com.dev.skindec.databinding.ActivityHomeBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    companion object {
        val TAG: String = HomeActivity::class.java.simpleName
        const val EXTRA_ID = "extra_id"
        const val EXTRA_IMAGE = "extra_image"
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var filePath: Uri
    private var id: Int = 0
    private var isPicture: Boolean = false
    private lateinit var image: String

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
            validateInput()
        }

        binding.ivRemove.setOnClickListener {
            isPicture = false
            binding.ivImage.setImageResource(R.drawable.empty_picture)
            binding.ivRemove.visibility = View.INVISIBLE
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
                isPicture = true
                binding.ivRemove.visibility = View.VISIBLE
                image = filePath.toString()
            }
            ImagePicker.RESULT_ERROR -> {
                Snackbar.make(
                    binding.btnUpload,
                    ImagePicker.getError(data),
                    Snackbar.LENGTH_SHORT
                ).show()
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

    private fun userRegister(userObject: JsonObject) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnUpload.visibility = View.GONE

        val client = ApiConfig.apiService().register(userObject)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    binding.progressBar.visibility =
                        View.INVISIBLE
                    id = response.body()?.id!!

                    val intent =
                        Intent(
                            this@HomeActivity,
                            ResultActivity::class.java
                        )
                    intent.putExtra(EXTRA_ID, id)
                    intent.putExtra(EXTRA_IMAGE, image)
                    startActivity(intent)
                    finish()
                } else {
                    binding.progressBar.visibility =
                        View.INVISIBLE
                    binding.btnUpload.visibility = View.VISIBLE
                    Snackbar.make(
                        binding.btnUpload,
                        "Upload gagal: " + response.message(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}"
                    )
                }
            }

            override fun onFailure(
                call: Call<UserResponse>,
                t: Throwable
            ) {
                binding.progressBar.visibility =
                    View.INVISIBLE
                binding.btnUpload.visibility = View.VISIBLE
                Snackbar.make(
                    binding.btnUpload,
                    "Upload gagal: " + t.message.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                Log.e(
                    TAG,
                    "onFailure: ${t.message.toString()}"
                )
            }
        })
    }

    private fun validateInput() {
        when {
            binding.etName.text.isEmpty() -> {
                binding.etName.error = "Nama tidak boleh kosong"
                binding.etName.requestFocus()
            }
            binding.etAge.text.isEmpty() -> {
                binding.etAge.error = "Kolom tidak boleh kosong"
                binding.etAge.requestFocus()
            }
            isPicture == false -> {
                Snackbar.make(
                    binding.root,
                    "Ambil gambar terlebih dahulu",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> {
                val name = binding.etName.text.toString()
                val age = binding.etAge.text.toString()
                lateinit var sex: String

                val selectedBtn =
                    binding.radioGroup.checkedRadioButtonId

                sex = if (selectedBtn == binding.rbLaki.id) {
                    binding.rbLaki.text.toString()
                } else {
                    binding.rbPerempuan.text.toString()
                }

                val userObject = JsonObject()
                userObject.addProperty("age", age)
                userObject.addProperty("name", name)
                userObject.addProperty("sex", sex)

                userRegister(userObject)
            }
        }
    }
}