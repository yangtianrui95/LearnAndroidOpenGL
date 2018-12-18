package com.yangtianrui.learnandroidopengl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.yangtianrui.learnandroidopengl.glviews.TrianglesGLView
import com.yangtianrui.learnandroidopengl.glviews.LinesGLView
import com.yangtianrui.learnandroidopengl.glviews.PointersGLView
import kotlinx.android.synthetic.main.activity_primitive.*

class PrimitiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primitive)
    }

    fun onPointsClick(v: View) {
        fl_content.removeAllViews()
        fl_content.addView(PointersGLView(context = this))
    }


    fun onLinesClick(v: View) {
        fl_content.removeAllViews()
        fl_content.addView(LinesGLView(context = this))
    }

    fun onTrianglesClick(v: View) {
        fl_content.removeAllViews()
        fl_content.addView(TrianglesGLView(context = this))
    }
}
