package com.toshiro97.loadmorejava.api

import com.toshiro97.loadmorejava.model.MoviesResponse
import io.reactivex.Observable

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieAPI {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") pageIndex: Int): Observable<MoviesResponse>


}
