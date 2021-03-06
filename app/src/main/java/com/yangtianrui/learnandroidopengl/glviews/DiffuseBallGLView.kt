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
 * 散射光效果
 * @author yangtianrui
 * @date 2018/12/31
 */
class DiffuseBallGLView : BallGLView {

    class Factory : IViewFactory {
        override fun create(context: Context): View {
            return DiffuseBallGLView(context)
        }
    }

    private val minimumLightX = -4f
    private var mLightX: Float = minimumLightX

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun getVertexShader() = "diffuse_ball_vertex.glsl"

    override fun getFragmentShader() = "diffuse_ball_frag.glsl"

    override fun onDrawCalls() {
        val uLocation = GLES30.glGetUniformLocation(mProgram, "uLocation")
        val uTransformMatrix = GLES30.glGetUniformLocation(mProgram, "uTransformMatrix")
        GLES30.glUniform3f(uLocation, mLightX, 0f, 3f)
        GLES30.glUniformMatrix4fv(uTransformMatrix, 1, false, mUMatrix, 0)
        super.onDrawCalls()
    }

    fun setLightLocation(max: Int?, progress: Int) {
        mLightX = 2 * Math.abs(minimumLightX) * ((progress.toFloat() / max!!) - .5f)
        Log.d(tag, "light location = $mLightX")
        requestRender()
    }
}