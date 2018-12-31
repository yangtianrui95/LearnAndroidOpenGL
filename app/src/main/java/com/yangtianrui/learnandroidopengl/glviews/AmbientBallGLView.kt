package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yangtianrui.learnandroidopengl.utils.IViewFactory

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * 环境光效果
 * @author yangtianrui
 * @date 2018/12/31
 */
class AmbientBallGLView : BallGLView {

    class Factory : IViewFactory {
        override fun create(context: Context): View {
            return AmbientBallGLView(context)
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    private val minimProgress = .1f
    private var mProgress: Float = minimProgress

    override fun getVertexShader() = "ball_vertex.glsl"

    override fun getFragmentShader() = "ambient_ball_frag.glsl"


    override fun onDrawCalls() {
        val uAmbient = GLES30.glGetUniformLocation(mProgram, "uAmbient")
        GLES30.glUniform4f(uAmbient, mProgress, mProgress, mProgress, mProgress)
        super.onDrawCalls()
    }

    fun setAmbient(progress: Int) {
        val factor = progress * .01f
        mProgress = if (factor < minimProgress) minimProgress else factor
        requestRender()
    }
}