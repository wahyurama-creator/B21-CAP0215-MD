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
        when {
            prediction?.equals("oily", true) == true -> {
                setDataOil()
            }
            prediction?.equals("acne", true) == true -> {
                setDataAcne()
            }
            prediction?.equals("normal", true) == true -> {
                setDataNormal()
            }
            prediction?.equals("dry", true) == true -> {
                setDataDry()
            }
            prediction?.equals("scar", true) == true -> {
                setDataScar()
            }
        }
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
                        )

                        contentTvSaranProduk.text =
                            resources.getString(
                                R.string.saran_produk,
                                prediction
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
                "KLEVERU Sea Buckthorn Cleansing Gel",
                R.drawable.skin_oily_one,
                "Kleveru Sea Buckthorn Cleansing Gel merupakan pembersih wajah yang diformulasikan sesuai pH kulit alami",
                "Rp75.000"
            )
        )
        listProduct.add(
            Product(
                "SOMETHINC Niacinamide + Moisture Beet Serum",
                R.drawable.skin_oily_two,
                "Melembabkan, mengatasi kemerahan, serta melawan jerawat",
                "Rp115.000"
            )
        )
        listProduct.add(
            Product(
                "The Aesthetics Skin X Dion Mulya Moist In Jar",
                R.drawable.skin_oily_three,
                "Merupakan pelembab wajah yang mengandung aloe vera",
                "Rp15.000"
            )
        )
        productAdapter.setProduct(listProduct)
    }

    private fun setDataAcne() {
        val listProduct = ArrayList<Product>()
        listProduct.add(
            Product(
                "Sensatia Botanicals Soapless Facial Cleanser",
                R.drawable.skin_sensitive_one,
                "Pembersih tanpa wewangian ini efektif mengangkat kotoran",
                "Rp130.000"
            )
        )
        listProduct.add(
            Product(
                "SKIN1004 Madagascar Centella Asiatica Serum",
                R.drawable.skin_sensitive_two,
                "Terbuat dari 100% ekstrak centella asiatica yang aman bagi kulit",
                "Rp170.000"
            )
        )
        listProduct.add(
            Product(
                "SAFI Dermasafe Day Moisturizer Soothe & Hydrate",
                R.drawable.skin_sensitive_three,
                "Krim pagi dengan tekstur gel yang mengandung Asam Hyaluronic",
                "Rp125.000"
            )
        )
        productAdapter.setProduct(listProduct)
    }

    private fun setDataNormal() {
        val listProduct = ArrayList<Product>()
        listProduct.add(
            Product(
                "Cetaphil Gentle Skin Cleanser",
                R.drawable.skin_dry_one,
                "Pembersih untuk kulit berminyak, keringg, maupun jerawat",
                "Rp145.000"
            )
        )
        listProduct.add(
            Product(
                "AVOSKIN YOUR SKIN BAE SERIES",
                R.drawable.skin_normal_two,
                "Membantu mencerahkan kulit dan menyamarkan bekas jerawat.",
                "Rp158.000"
            )
        )
        listProduct.add(
            Product(
                "Kiehls Ultra Facial Cream",
                R.drawable.skin_normal_three,
                "Merupakan pelembap dengan tekstur lembut yang mampu melembapkan wajah",
                "Rp299.000"
            )
        )
        productAdapter.setProduct(listProduct)
    }

    private fun setDataDry() {
        val listProduct = ArrayList<Product>()
        listProduct.add(
            Product(
                "Cetaphil Gentle Skin Cleanser",
                R.drawable.skin_dry_one,
                "Pembersih untuk kulit berminyak, keringg, maupun jerawat",
                "Rp145.000"
            )
        )
        listProduct.add(
            Product(
                "SOMETHINC Serum",
                R.drawable.skin_dry_two,
                "Menghidrasi kulit agar tampak lebih segar",
                "Rp115.000"
            )
        )
        listProduct.add(
            Product(
                "Cerave Moisturizing Cream",
                R.drawable.skin_dry_three,
                "Efektif untuk menghidrasi dan melindungi kulit",
                "Rp389.000"
            )
        )
        productAdapter.setProduct(listProduct)
    }

    private fun setDataScar() {
        val listProduct = ArrayList<Product>()
        listProduct.add(
            Product(
                "Sensatia Botanicals Soapless Facial Cleanser",
                R.drawable.skin_sensitive_one,
                "Pembersih tanpa wewangian ini efektif mengangkat kotoran",
                "Rp130.000"
            )
        )
        listProduct.add(
            Product(
                "SKIN1004 Madagascar Centella Asiatica Serum",
                R.drawable.skin_sensitive_two,
                "Terbuat dari 100% ekstrak centella asiatica yang aman bagi kulit",
                "Rp170.000"
            )
        )
        listProduct.add(
            Product(
                "SAFI Dermasafe Day Moisturizer Soothe & Hydrate",
                R.drawable.skin_sensitive_three,
                "Krim pagi dengan tekstur gel yang mengandung Asam Hyaluronic",
                "Rp125.000"
            )
        )
        productAdapter.setProduct(listProduct)
    }
}