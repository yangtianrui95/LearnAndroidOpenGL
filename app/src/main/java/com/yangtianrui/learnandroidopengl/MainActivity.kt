package com.yangtianrui.learnandroidopengl

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.yangtianrui.learnandroidopengl.glviews.CubeGLView
import com.yangtianrui.learnandroidopengl.glviews.HexagramGLView
import com.yangtianrui.learnandroidopengl.utils.IViewFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onPrimitiveClick(view: View) {
        startActivity(Intent(this, PrimitiveActivity::class.java))
    }


    fun onProjectionClick(view: View) {
        startGLActivity(view, HexagramGLView.Factory())
    }


    fun onDrawCubeClick(view: View) {
        startGLActivity(view, CubeGLView.Factory())
    }


    private fun startGLActivity(view: View, factory: IViewFactory) {
        val intent = Intent(this, GLViewActivity::class.java)
        intent.putExtra(GLViewActivity.extra_factory, factory)
        intent.putExtra(GLViewActivity.extra_title, (view as? TextView)?.text)
        startActivity(intent)
    }
}


