package com.yangtianrui.learnandroidopengl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yangtianrui.learnandroidopengl.utils.IViewFactory

class GLViewActivity : AppCompatActivity() {

    companion object {
        const val extra_key = "com.yangtianrui.learnandroidopengl.EXTRA_VIEW_FACTORY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewFactory:IViewFactory = intent.getSerializableExtra(extra_key) as IViewFactory
        setContentView(viewFactory.create(this))
    }
}
