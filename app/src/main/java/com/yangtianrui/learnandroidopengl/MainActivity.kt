package com.yangtianrui.learnandroidopengl

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onPrimitiveClick(view: View) {
        startActivity(Intent(this, PrimitiveActivity::class.java))
    }


    fun onProjectionClick(view: View) {
        startActivity(Intent(this, FrustumActivity::class.java))
    }
}


