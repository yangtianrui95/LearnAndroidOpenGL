package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.support.annotation.CallSuper
import android.util.AttributeSet
import android.util.Log
import com.yangtianrui.learnandroidopengl.glutils.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * <p>
 *     OpenGL绘制封装
 *
 * @author yangtianrui
 * @date 2018/12/10
 */

@Suppress("LeakingThis", "MemberVisibilityCanBePrivate")
abstract class AbsPrimitiveGLView : GLSurfaceView, GLSurfaceView.Renderer {

    protected val tag = "AbsPrimitiveGLView"
    protected var mProgram: Int = -1

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        setEGLContextClientVersion(3)
        setRenderer(this)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    @CallSuper
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        loadShader()
    }

    @CallSuper
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }


    @CallSuper
    override fun onDrawFrame(gl: GL10?) {
        GLES30.glUseProgram(mProgram)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
    }

    protected abstract fun getVertexShader(): String

    protected abstract fun getFragmentShader(): String


    private fun loadShader() {
        val fragment = ShaderUtils.loadFromAssetsFile(getFragmentShader(), resources)
        val vertex = ShaderUtils.loadFromAssetsFile(getVertexShader(), resources)
        mProgram = ShaderUtils.createProgram(vertex, fragment)
        if (mProgram < 0) {
            Log.e(tag, "link shader fail")
        }
        Log.d(tag, "program: $mProgram")
    }

}