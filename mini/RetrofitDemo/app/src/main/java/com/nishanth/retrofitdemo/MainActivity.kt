package com.nishanth.retrofitdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var textViewResult: TextView
    lateinit var jsonPlaceHolderApi: JsonPlaceHolderApi
    lateinit var retrofit: Retrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewResult = findViewById(R.id.text_view_result)
        val loggingInterceptor=HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient=OkHttpClient.Builder()
                .addInterceptor(object:Interceptor{
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val originalRequest=chain.request()
                        val finalRequest=originalRequest.newBuilder()
                                .header("Interceptor-Header","XYZ")
                                .build()
                        return chain.proceed(finalRequest)
                    }

                })
                .addInterceptor(loggingInterceptor)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
        //getPosts()
        //createPosts()
        updatePost()
        //deletePost()
    }

    private fun deletePost() {
        val call = jsonPlaceHolderApi.deletePost(5)
        call.enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                textViewResult.setText("Code : ${response.code()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                textViewResult.setText(t.message)
            }

        })
    }

    private fun updatePost() {
        val post=Post("3", null, "This is text for updating post")
        val call = jsonPlaceHolderApi.putPost("abc",5,post)
        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    textViewResult.setText("Code : ${response.code()}")
                    return;
                }
                textViewResult.setText("")
                val post = response.body()
                var content = ""
                content += "Code : ${response.code()}\n"
                content += "Id: ${post!!.id}\n"
                content += "UserId: ${post.userId}\n"
                content += "Title: ${post.title}\n"
                content += "Text: ${post.text}\n\n\n"
                textViewResult.append(content)
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                textViewResult.setText(t.message)
            }

        })
    }

    private fun createPosts() {
        val call = jsonPlaceHolderApi.createPost("5", "Title", "I don't know what to put under text")
        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    textViewResult.setText("Code : ${response.code()}")
                    return;
                }
                textViewResult.setText("")
                val post = response.body()
                var content = ""
                content += "Code : ${response.code()}\n"
                content += "Id: ${post!!.id}\n"
                content += "UserId: ${post.userId}\n"
                content += "Title: ${post.title}\n"
                content += "Text: ${post.text}\n\n\n"
                textViewResult.append(content)
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                textViewResult.setText(t.message)
            }

        })
    }

    private fun getPosts() {
        val call = jsonPlaceHolderApi.getPosts(3, "id", "desc")
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    textViewResult.setText("Code : ${response.code()}")
                    return;
                }
                textViewResult.setText("")
                val posts = response.body()
                for (post in posts!!) {
                    var content = ""
                    content += "Id: ${post.id}\n"
                    content += "UserId: ${post.userId}\n"
                    content += "Title: ${post.title}\n"
                    content += "Text: ${post.text}\n\n\n"
                    textViewResult.append(content)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                textViewResult.setText(t.message)
            }

        })
    }
}