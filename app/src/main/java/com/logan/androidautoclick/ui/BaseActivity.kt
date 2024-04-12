package com.logan.androidautoclick.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.androidautoclick.R
import com.logan.androidautoclick.ui.uitils.immersive

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.immersive(resources.getColor(R.color.color_283B5B), false)
    }
}