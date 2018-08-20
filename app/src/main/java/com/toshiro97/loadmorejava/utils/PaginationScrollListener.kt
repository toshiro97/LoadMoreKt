package com.toshiro97.loadmorejava.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


abstract class PaginationScrollListener(internal var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    abstract fun totalPageCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= totalPageCount()) {
                loadMoreItems()
            }
        }

    }


    protected abstract fun loadMoreItems()

}
