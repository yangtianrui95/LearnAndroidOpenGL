package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yangtianrui.learnandroidopengl.utils.IViewFactory

/**
 * Copyright (C) 2019 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2019/1/12
 */
class SpecularGLView : BallGLView {


    class SpecularFactory : IViewFactory {
        override fun create(context: Context): View {
            return SpecularGLView(context)
        }
    }


    private val minimumLightX = -4f
    private val minimumShininess = 1f
    private val maximumShininess = 50f
    private var mLightX: Float = minimumLightX
    private var mShininess: Float = minimumShininess

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun getVertexShader() = "specular_ball_vertex.glsl"

    override fun getFragmentShader() = "specular_ball_frag.glsl"


    override fun onDrawCalls() {
        val uCamera = GLES30.glGetUniformLocation(mProgram, "uCamera")
        val uLocation = GLES30.glGetUniformLocation(mProgram, "uLocation")
        val uTransformMatrix = GLES30.glGetUniformLocation(mProgram, "uTransformMatrix")
        val uShininess = GLES30.glGetUniformLocation(mProgram, "uShininess")

        GLES30.glUniform1f(uShininess, mShininess)
        GLES30.glUniform3f(uLocation, mLightX, 0f, 3f)
        GLES30.glUniformMatrix4fv(uTransformMatrix, 1, false, mUMatrix, 0)
        GLES30.glUniform3f(uCamera, mCameraLocation[0], mCameraLocation[1], mCameraLocation[2])

        Log.d(tag, "uCamera location : $uCamera, uLocation $uLocation, uTransformMatrix $uTransformMatrix")
        super.onDrawCalls()
    }

    fun setShininess(progress: Int, max: Int?) {
        val factor = progress.toFloat() / max!!
        mShininess = maximumShininess / minimumShininess * factor + minimumShininess
        Log.d(tag, "shininess: $mShininess")
        requestRender()
    }

}