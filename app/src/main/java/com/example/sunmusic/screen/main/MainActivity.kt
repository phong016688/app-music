package com.example.sunmusic.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sunmusic.R
import com.example.sunmusic.utils.replaceFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(MainFragment.newInstance(), R.id.container)
    }
}
