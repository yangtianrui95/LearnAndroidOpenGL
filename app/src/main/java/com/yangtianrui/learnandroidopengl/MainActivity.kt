package com.yangtianrui.learnandroidopengl

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.yangtianrui.learnandroidopengl.glviews.BallGLView
import com.yangtianrui.learnandroidopengl.glviews.CubeGLView
import com.yangtianrui.learnandroidopengl.glviews.HexagramGLView
import com.yangtianrui.learnandroidopengl.glviews.TriangleTextureGLView
import com.yangtianrui.learnandroidopengl.utils.ISeekBarListener
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
        startGLActivity(view, CubeGLView.Factory(), object : ISeekBarListener {
            override fun onProgressChanged(view: View?, progress: Int) {
                if (view is CubeGLView) {
                    view.setCameraMatrix(progress)
                }
            }
        })
    }

    fun onTextureClick(view: View) {
        startGLActivity(view, TriangleTextureGLView.Factory())
    }


    fun onDrawBallClick(view: View){
        startGLActivity(view, BallGLView.Factory())
    }

    private fun startGLActivity(view: View, factory: IViewFactory, seekbarListener: ISeekBarListener? = null) {
        val intent = Intent(this, GLViewActivity::class.java)
        intent.putExtra(GLViewActivity.extra_factory, factory)
        intent.putExtra(GLViewActivity.extra_title, (view as? TextView)?.text)
        intent.putExtra(GLViewActivity.extra_seek, seekbarListener)
        startActivity(intent)
    }
}


