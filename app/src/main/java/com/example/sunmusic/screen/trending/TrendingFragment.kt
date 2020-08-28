package com.example.sunmusic.screen.trending

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sunmusic.R

class TrendingFragment : Fragment(R.layout.fragment_trending) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrendingFragment()
    }
}
