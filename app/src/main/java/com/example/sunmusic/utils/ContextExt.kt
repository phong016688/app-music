package com.example.sunmusic.utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
