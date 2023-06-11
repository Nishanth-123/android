package com.example.custom_camera.models

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImagesApi {

    @POST("auth/sign_in")
    fun signIn(
            @HeaderMap headers: Map<String, String>,
            @Body body: SigninBody
    ): Call<ResponseBody>

    @Multipart
    @POST("tests")
    fun saveImage(
            @HeaderMap headers: Map<String, String>,
            @Part("test[done_date]") doneDate: String,
            @Part imagePart: MultipartBody.Part,
            @Part("test[batch_qr_code]") batchQrCode: String,
            @Part("test[reason]") reason: String,
            @Part("test[failure]") failure: Boolean
    ): Call<ResponseBody>

}