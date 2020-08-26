package com.example.sunmusic.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sunmusic.R

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter()
        presenter.setView(this)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
