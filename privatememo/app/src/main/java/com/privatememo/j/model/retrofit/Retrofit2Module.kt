package com.privatememo.j.model.retrofit

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class Retrofit2Module {

    var baseurl = "http://edit0.dothome.co.kr/"

    companion object mo {
        private var instance: Retrofit2Module? = null

        @JvmStatic
        fun getInstance(): Retrofit2Module = instance
            ?: synchronized(this){
            instance
                ?: Retrofit2Module().also {
                instance = it
            }
        }
    }

    fun BaseModule(): Retrofit2API {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseurl)
                .build()
        val client: Retrofit2API = retrofit.create(
            Retrofit2API::class.java)

        return client
    }

    fun SendImageModule(file: File, fileName: String): Pair<Retrofit2API, MultipartBody.Part>{
        var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/jpg"),file)
        var body : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file", fileName, requestBody)

        var retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseurl)
                .build()

        var server = retrofit.create(Retrofit2API::class.java)

        return Pair(server,body)
    }


}