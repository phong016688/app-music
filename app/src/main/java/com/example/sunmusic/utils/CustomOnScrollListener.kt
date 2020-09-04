package com.example.sunmusic.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {
    private var previousTotalItemCount = 0
    private var isLoading = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0

        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
        }

        if (isLoading && layoutManager.itemCount > previousTotalItemCount) {
            isLoading = false
            previousTotalItemCount = layoutManager.itemCount
        }

        if (!isLoading && lastVisibleItemPosition + VISIBLE_THRESHOLD > layoutManager.itemCount) {
            isLoading = true
            onLoadMore()
        }
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 3
    }
}
