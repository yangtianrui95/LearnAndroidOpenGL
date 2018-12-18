package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.util.AttributeSet
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * <p>
 *     线的绘制
 *
 * @author yangtianrui
 * @date 2018/12/11
 */

class LinesGLView : AbsPrimitiveGLView {


    private val vertexArray = floatArrayOf(-.5f, 0f, 0f,
            0f, .5f, 1f,
            0f, -.5f, 0f,
            .5f, 0f, 0f
    )


    private val vertexBuffer = BufferUtils.createBuffer(vertexArray)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun getVertexShader() = "line_vertex.glsl"
    override fun getFragmentShader() = "pointer_frag.glsl"

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        GLES30.glClearColor(1f, 0f, 0f, 0f)

        val positionLocation = GLES30.glGetAttribLocation(mProgram, "position")
        GLES30.glVertexAttribPointer(positionLocation, 3
                , GLES30.GL_FLOAT, false, 4 * 3, vertexBuffer)
        GLES30.glEnableVertexAttribArray(positionLocation)

        GLES30.glLineWidth(20f)
        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, vertexArray.size / 3)
        GLES30.glFlush()
    }
}