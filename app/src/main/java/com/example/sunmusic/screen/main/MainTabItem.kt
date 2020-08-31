package com.example.sunmusic.screen.main

import androidx.fragment.app.Fragment

data class MainTabItem(val title: String, val fragment: Fragment) {

    companion object {
        const val POSITION_HOME = 0
        const val POSITION_TRENDING = 1
        const val POSITION_ALBUM = 2
        const val POSITION_GENRES = 3
    }
}
