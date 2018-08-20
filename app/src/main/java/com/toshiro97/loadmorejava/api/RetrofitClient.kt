package com.toshiro97.loadmorejava.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val BASE_URL = "http://api.themoviedb.org/3/"
    var retrofit: Retrofit? = null


    val client: Retrofit
        get() {

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return retrofit!!
        }
}
