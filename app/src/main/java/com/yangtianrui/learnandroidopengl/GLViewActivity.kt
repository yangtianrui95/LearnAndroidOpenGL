package com.yangtianrui.learnandroidopengl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yangtianrui.learnandroidopengl.utils.IViewFactory

class GLViewActivity : AppCompatActivity() {

    companion object {
        const val extra_factory = "com.yangtianrui.learnandroidopengl.GLViewActivity.EXTRA_VIEW_FACTORY"
        const val extra_title = "com.yangtianrui.learnandroidopengl.GLViewActivity.EXTRA_TITLE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewFactory:IViewFactory = intent.getSerializableExtra(extra_factory) as IViewFactory
        setContentView(viewFactory.create(this))
        setTitleFromIntent()
    }

    private fun setTitleFromIntent() {
        val title = intent.getStringExtra(extra_title)
        if (title != null) {
            setTitle(title)
        }
    }
}
