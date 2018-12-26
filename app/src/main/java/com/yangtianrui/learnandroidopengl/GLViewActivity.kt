package com.yangtianrui.learnandroidopengl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import com.yangtianrui.learnandroidopengl.utils.ISeekBarListener
import com.yangtianrui.learnandroidopengl.utils.IViewFactory
import kotlinx.android.synthetic.main.activity_glview.*

class GLViewActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    companion object {
        const val extra_factory = "com.yangtianrui.learnandroidopengl.GLViewActivity.EXTRA_VIEW_FACTORY"
        const val extra_title = "com.yangtianrui.learnandroidopengl.GLViewActivity.EXTRA_TITLE"
        const val extra_seek = "com.yangtianrui.learnandroidopengl.GLViewActivity.EXTRA_SEEK"
    }

    private var mSeekBarListener: ISeekBarListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glview)
        seekbar.setOnSeekBarChangeListener(this)
        initFromIntent()
    }

    private fun initFromIntent() {
        if (intent == null) {
            return
        }
        setGLViewFromIntent()
        setTitleFromIntent()
        setSeekListenerFromIntent()
    }

    private fun setSeekListenerFromIntent() {
        mSeekBarListener = intent.getSerializableExtra(extra_seek) as ISeekBarListener?
        if (mSeekBarListener == null) {
            seekbar.visibility = View.GONE
        }
    }

    private fun setGLViewFromIntent() {
        val viewFactory: IViewFactory = intent.getSerializableExtra(extra_factory) as IViewFactory
        fl_content.addView(viewFactory.create(this))
    }

    private fun setTitleFromIntent() {
        val title = intent.getStringExtra(extra_title)
        if (title != null) {
            setTitle(title)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        mSeekBarListener?.onProgressChanged(fl_content.getChildAt(0), progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
