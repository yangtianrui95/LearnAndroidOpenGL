package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.util.AttributeSet
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * <p>
 *
 *     以Triangle为例，演示DrawElement 绘制
 *
 * @author yangtianrui
 * @date 2018/12/15
 */

open class TrianglesGLView : AbsPrimitiveGLView {

    private val vertexArray = floatArrayOf(
            -.5f, 0f, 0f,
            0f, .5f, 1f,
            0f, -.5f, 0f,
            .5f, 0f, 0f
    )

    /// 顶点索引数组，绘制0,2,3顶点
    private val vertexIndexArray = byteArrayOf(0, 2, 3)
    private val buffer = BufferUtils.createBuffer(vertexArray)
    private val indexBuffer = BufferUtils.createBuffer (vertexIndexArray)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)


    override fun getVertexShader() = "line_vertex.glsl"

    override fun getFragmentShader() = "pointer_frag.glsl"


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        val location = GLES30.glGetAttribLocation(mProgram, "position")
        GLES30.glVertexAttribPointer(location, 3, GLES30.GL_FLOAT, false, 3 * 4, buffer)
        GLES30.glEnableVertexAttribArray(location)
        drawTriangle()
    }

    protected open fun drawTriangle() {
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, vertexIndexArray.size, GLES30.GL_UNSIGNED_BYTE, indexBuffer)
    }
}