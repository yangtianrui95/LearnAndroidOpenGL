package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.util.AttributeSet
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * <p>
 *
 * @author yangtianrui
 * @date 2018/12/11
 */

class PointersGLView : AbsPrimitiveGLView {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        GLES30.glClearColor(1f, 0f, 0f, 1f);
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 1)
        GLES30.glFlush()
    }

    override fun getVertexShader(): String = "pointer_vertex.glsl"

    override fun getFragmentShader(): String = "pointer_frag.glsl"
}