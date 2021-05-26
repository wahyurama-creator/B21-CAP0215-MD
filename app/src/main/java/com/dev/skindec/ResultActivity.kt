package com.dev.skindec

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dev.skindec.databinding.ActivityResultBinding
import com.dev.skindec.home.HomeViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserById(1)

        viewModel.isLoading.observe(this, {
            binding.progressBar.visibility =
                if (it) View.VISIBLE else View.GONE
        })

        viewModel.user.observe(this, { user ->
            binding.tvNameResult.text = user.name
            binding.tvSkinResult.text = user.skinType
        })
    }
}