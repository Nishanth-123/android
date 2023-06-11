package com.nishanth.retrofitdemo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JsonPlaceHolderApi {
    @GET("posts")
    fun getPosts(@Query("userId") userId:Int,
    @Query("_sort") sort:String,
    @Query("_order") order:String):Call<List<Post>>

    @POST("posts")
    fun createPost(@Body post: POST):Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(@Field("userId") userId:String,
                   @Field("title") title:String,
                   @Field("body") text:String, ):Call<Post>

    @PUT("posts/{id}")
    fun putPost(@Header("Dynamic-Header") dHeader:String, @Path("id") id: Int, @Body post: Post):Call<Post>

    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body post: Post):Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int):Call<Void>
}