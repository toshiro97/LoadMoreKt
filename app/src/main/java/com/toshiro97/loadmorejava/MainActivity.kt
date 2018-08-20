package com.toshiro97.loadmorejava

import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar

import com.toshiro97.loadmorejava.api.RetrofitClient
import com.toshiro97.loadmorejava.api.IMovieAPI
import com.toshiro97.loadmorejava.model.Movie
import com.toshiro97.loadmorejava.model.MoviesResponse
import com.toshiro97.loadmorejava.utils.PaginationScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    internal lateinit var adapter: PaginationAdapter
    internal lateinit var linearLayoutManager: LinearLayoutManager
    private var compositeDisposable: CompositeDisposable? = null

    internal lateinit var rv: RecyclerView
    internal lateinit var progressBar: ProgressBar
    private var isLoading = false
    private var isLastPage = false
    private val PAGE_START = 1
    private val TOTAL_PAGES = 10
    private var currentPage = PAGE_START

    private var movieService: IMovieAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compositeDisposable = CompositeDisposable()

        rv = findViewById(R.id.main_recycler)
        progressBar = findViewById(R.id.main_progress)

        adapter = PaginationAdapter(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = linearLayoutManager

        rv.itemAnimator = DefaultItemAnimator()

        rv.adapter = adapter

        rv.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun totalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun loadMoreItems() {
                this@MainActivity.isLoading = true
                currentPage += 1

                Handler().postDelayed({ loadNextPage() }, 1000)
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        val retrofit =RetrofitClient.client

        movieService = retrofit.create(IMovieAPI::class.java)

        loadFirstPage()

    }

    private fun loadFirstPage() {
        compositeDisposable?.add(movieService!!.getPopularMovies("a88ae82fe965d2aab2e955e0217d02cb",PAGE_START)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{movies->displayData(movies)}
        )
    }

    private fun displayData(movies: MoviesResponse?) {
        val results = movies!!.results
        progressBar.visibility = View.GONE
        adapter.addAll(results!!)

        if (currentPage <= TOTAL_PAGES)
            adapter.addLoadingFooter()
        else
            isLastPage = true
    }

    private fun loadNextPage() {
        compositeDisposable?.add(movieService!!.getPopularMovies("a88ae82fe965d2aab2e955e0217d02cb",currentPage+1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{movies->displayNextData(movies)}
        )
    }

    private fun displayNextData(movies: MoviesResponse?) {
        adapter.removeLoadingFooter()
        isLoading = false
        var listMovie : MutableList<Movie> = movies!!.results as MutableList<Movie>
        adapter.addAll(listMovie)
        if (currentPage <= TOTAL_PAGES)
            adapter.addLoadingFooter()
        else
            isLastPage = true
        adapter.notifyDataSetChanged()
    }

}