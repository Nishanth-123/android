package com.example.custom_camera

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.custom_camera.models.SignInResponse
import com.example.custom_camera.models.SigninBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Math.abs
import kotlin.math.roundToInt


class TestViewModel : ViewModel() {

    private val imagePathsToEV = mutableMapOf<String, Int>()
    private val remoteRepository = RemoteRepository(NetworkModule.provideApiService())
    private val signInResponse = MutableLiveData<SignInResponse>()
    val sendImageResponse = MutableLiveData<Boolean?>()

    //bad practice to store credentials
    private val email = "amit_4@test.com"
    private val password = "12345678"

    fun savePicture(data: ByteArray, exposureValue: Int?): Boolean {
        val filename = "image_" + System.currentTimeMillis() + ".jpg"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, filename)
        return try {
            FileOutputStream(imageFile).use { output ->
                output.write(data)
            }
            if (exposureValue != null) {
                imagePathsToEV[imageFile.absolutePath] = exposureValue
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun sendImage() {
        val meanExposure = calculateMeanExposure() ?: throw RuntimeException("No captured images to send")
        val imagePathToBeSent = findImageToBeSent(meanExposure)
        signIn()
        //val imageFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "image_1679282802091.jpg")
        setUpSendImageCall(imagePathToBeSent)
        //setUpSendImageCall(imageFile.absolutePath)
    }

    private fun setUpSendImageCall(imagePath: String) {
        signInResponse.observeForever { signInResponse ->
            val headers = mapOf(
                    "Content-Type" to "application/json",
                    "access-token" to signInResponse.accessToken,
                    "uid" to signInResponse.uid,
                    "client" to signInResponse.client,
                    "Accept" to "*/*",
                    "Host" to "apistaging.inito.com",
            )
            val file = File(imagePath)
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val imagePart = MultipartBody.Part.createFormData("test[images_attributes][][pic]", file.name, requestFile)
            val call = remoteRepository.saveImage(
                    headers,
                    System.currentTimeMillis().toString(),
                    imagePart,
                    "AAO",
                    "NA",
                    false
            )
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                    if (response.isSuccessful) {
                        sendImageResponse.postValue(true)
                    } else {
                        sendImageResponse.postValue(false)
                        // Handle error response
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                    sendImageResponse.postValue(false)
                    println("Could not send message because of : ${t!!.message}")
                }
            })
        }
    }

    private fun signIn() {
        val headers = mapOf("Content-Type" to "application/json")
        val signInBody = SigninBody(email, password)
        val call = remoteRepository.signIn(headers, signInBody)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    val uid = response.headers()["uid"]!!
                    val client = response.headers()["client"]!!
                    val accessToken = response.headers()["access-token"]!!
                    signInResponse.postValue(SignInResponse(uid, accessToken, client))

                } else {
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                println("Could not send message because of : ${t!!.message}")
            }
        })
    }

    private fun calculateMeanExposure(): Int? {
        if (imagePathsToEV.isEmpty()) {
            return null
        }
        return (imagePathsToEV.values.sum().toFloat() / imagePathsToEV.values.size).roundToInt()
    }

    private fun findImageToBeSent(meanExposure: Int): String {
        var currentPath = imagePathsToEV.entries.first().key
        var currentEv = imagePathsToEV.entries.first().value
        imagePathsToEV.entries.forEach { entry ->
            if (abs(entry.value - meanExposure) < abs(currentEv - meanExposure)) {
                currentPath = entry.key
                currentEv = entry.value
            }
        }
        return currentPath
    }


}