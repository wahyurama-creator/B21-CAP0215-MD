package com.dev.skindec.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var productName: String,
    var productImage: Int,
    var description: String,
    var price: String
) : Parcelable
