package com.dev.skindec.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.skindec.core.data.model.Product
import com.dev.skindec.databinding.ItemProductBinding
import com.dev.skindec.result.ProductAdapter.ProductViewHolder

class ProductAdapter :
    RecyclerView.Adapter<ProductViewHolder>() {
    private var listProduct = ArrayList<Product>()

    fun setProduct(newProduct: List<Product>?) {
        if (newProduct == null) return
        listProduct.clear()
        listProduct.addAll(newProduct)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int = listProduct.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            with(binding) {
                Glide.with(binding.root)
                    .load(data.productImage)
                    .into(ivProduct)

                tvNameProduct.text = data.productName
                tvDescription.text = data.description
                tvPrice.text = data.price
            }
        }
    }
}