package com.example.custom_camera

import com.example.custom_camera.models.ImagesApi
import com.example.custom_camera.models.SigninBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call

class RemoteRepository constructor(
        private val imagesApi: ImagesApi
) {
    fun signIn(
            headers: Map<String, String>,
            body: SigninBody
    ): Call<ResponseBody> {
        return imagesApi.signIn(headers, body)
    }

    fun saveImage(
            headers: Map<String, String>,
            doneDate: String,
            imagePart: MultipartBody.Part,
            batchQrCode: String,
            reason: String,
            failure: Boolean
    ): Call<ResponseBody> {
        return imagesApi.saveImage(headers, doneDate, imagePart, batchQrCode, reason, failure)
    }
}