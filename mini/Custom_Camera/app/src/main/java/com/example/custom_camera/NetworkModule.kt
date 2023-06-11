package com.example.custom_camera

import com.example.custom_camera.models.ImagesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModule {
    companion object {
        private const val BASE_URL = "http://apistaging.inito.com/api/v1/"
        fun provideApiService(): ImagesApi {
            val retrofit = provideRetrofitInstance()
            return retrofit.create(ImagesApi::class.java)
        }

        private fun provideRetrofitInstance(): Retrofit {
            val okHttpClient = provideHttpClient()
            val gsonConverterFactory = provideConverterFactory()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .build()
        }

        private fun provideConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }

        private fun provideHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()

        }
    }
}