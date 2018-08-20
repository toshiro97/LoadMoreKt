package com.toshiro97.loadmorejava

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.toshiro97.loadmorejava.model.Movie

import java.util.ArrayList


class PaginationAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val movieResults: MutableList<Movie>?

    private var isLoadingAdded = false

    init {
        movieResults = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == ITEM)
            return MovieVH(inflater.inflate(R.layout.item_list, parent, false))
        else
            return LoadingVH(inflater.inflate(R.layout.item_progress, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val result = movieResults!![position]

        when (getItemViewType(position)) {
            ITEM -> {
                val movieVH = holder as MovieVH

                movieVH.mMovieTitle.text = result.title
                movieVH.mYear.text = result.releaseDate
                movieVH.mMovieDesc.text = result.overview

                Glide.with(context)
                        .load(BASE_URL_IMG + result.posterPath!!)
                        .into(movieVH.mPosterImg)
            }

            LOADING -> {
            }
        }

    }

    override fun getItemCount(): Int {
        return movieResults?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movieResults!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(r: Movie) {
        movieResults!!.add(r)
        notifyItemInserted(movieResults.size - 1)
    }

    fun addAll(moveResults: List<Movie>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Movie())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = movieResults!!.size - 1
        val result = getItem(position)

        if (result != null) {
            movieResults.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Movie? {
        return movieResults!![position]
    }


    protected inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val mMovieTitle: TextView
         val mMovieDesc: TextView
         val mYear: TextView
         val mPosterImg: ImageView

        init {
            mMovieTitle = itemView.findViewById(R.id.movie_title)
            mMovieDesc = itemView.findViewById(R.id.movie_desc)
            mYear = itemView.findViewById(R.id.movie_year)
            mPosterImg = itemView.findViewById(R.id.movie_poster)
        }
    }


    protected inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {

        private val ITEM = 0
        private val LOADING = 1
        private val BASE_URL_IMG = "https://image.tmdb.org/t/p/w150"
    }


}