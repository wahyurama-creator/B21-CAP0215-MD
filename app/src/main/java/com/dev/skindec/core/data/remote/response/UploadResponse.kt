package com.dev.skindec.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("prediction")
    var prediction: String
)