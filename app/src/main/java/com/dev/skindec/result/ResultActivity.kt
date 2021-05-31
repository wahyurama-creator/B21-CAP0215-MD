package com.dev.skindec.result

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dev.skindec.R
import com.dev.skindec.core.data.model.Product
import com.dev.skindec.core.data.remote.network.ApiConfig
import com.dev.skindec.core.data.remote.response.UserResponse
import com.dev.skindec.databinding.ActivityResultBinding
import com.dev.skindec.home.HomeActivity
import com.dev.skindec.home.HomeActivity.Companion.EXTRA_ID
import com.dev.skindec.home.HomeActivity.Companion.EXTRA_IMAGE
import com.dev.skindec.home.HomeActivity.Companion.EXTRA_PREDICTION
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var productAdapter: ProductAdapter
    private var prediction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val image = intent.getStringExtra(EXTRA_IMAGE)
        prediction = intent.getStringExtra(EXTRA_PREDICTION)

        productAdapter = ProductAdapter()

        Glide.with(this)
            .load(image)
            .circleCrop()
            .into(binding.ivImageResult)

        getUser(id)

        initRecyclerView()
        setDataOil()
    }

    private fun getUser(id: Int) {
        binding.progressBar.visibility = View.VISIBLE

        val client = ApiConfig.userService().getUser(id)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.INVISIBLE

                    val name = response.body()?.name

                    with(binding) {
                        tvNameResult.text =
                            resources.getString(
                                R.string.user_name,
                                name
                            )
                        tvSkinResult.text = resources.getString(
                            R.string.content_tipe_kulit,
                            prediction,
                            "silau"
                        )
                    }
                } else {
                    binding.progressBar.visibility = View.INVISIBLE

                    Snackbar.make(
                        binding.root,
                        "Gagal mengambil data: " + response.message(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.e(
                        HomeActivity.TAG,
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
                Snackbar.make(
                    binding.root,
                    "Gagal mengambil data: " + t.message.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                Log.e(
                    HomeActivity.TAG,
                    "onFailure: ${t.message.toString()}"
                )
            }

        })
    }

    private fun initRecyclerView() {
        binding.rvProduct.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setDataOil() {
        val listProduct = ArrayList<Product>()
        listProduct.add(
            Product(
                "Cetaphil Gentle Skin Cleanser",
                R.drawable.oil_skin_one,
                "Pembersih Muka",
                "Rp15.000"
            )
        )
        listProduct.add(
            Product(
                "Cetaphil Gentle Skin Cleanser",
                R.drawable.oil_skin_one,
                "Pembersih Muka",
                "Rp15.000"
            )
        )
        listProduct.add(
            Product(
                "Cetaphil Gentle Skin Cleanser",
                R.drawable.oil_skin_one,
                "Pembersih Muka",
                "Rp15.000"
            )
        )
        productAdapter.setProduct(listProduct)
    }
}