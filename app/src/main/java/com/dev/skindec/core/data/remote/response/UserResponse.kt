package com.dev.skindec.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("age")
    var age: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("sex")
    var sex: String,

    @SerializedName("skin_type")
    var skinType: String? = null
)
