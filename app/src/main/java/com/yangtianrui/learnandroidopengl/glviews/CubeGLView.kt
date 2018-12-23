package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import com.yangtianrui.learnandroidopengl.utils.IViewFactory
import java.util.*
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * 立方体绘制
 *
 * @author yangtianrui
 * @date 2018/12/18
 */
class CubeGLView : AbsPrimitiveGLView {

    class Factory : IViewFactory {
        override fun create(context: Context): View {
            return CubeGLView(context)
        }
    }


    private val vertexArray = floatArrayOf(
            0f, 0f, 0f,
            1f, 1f, 0f,
            -1f, 1f, 0f,
            -1f, -1f, 0f,
            1f, -1f, 0f,
            1f, 1f, 0f
    )

    private val vertexBuffer = BufferUtils.createBuffer(vertexArray)
    private val uMatrix = FloatArray(16)
    private val projection = FloatArray(16)
    private val lookupMatrix = FloatArray(16)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.setIdentityM(projection, 0)
    }

    override fun getVertexShader(): String = "cube_vertex.glsl"

    override fun getFragmentShader(): String = "pointer_frag.glsl"


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        val radio = if (width.toFloat() / height > 0) width.toFloat() / height else height.toFloat() / width
        // 正交投影的计算
        if (width > height) {
            Matrix.orthoM(projection, 0, -radio, radio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(projection, 0, -1f, 1f, -radio, radio, -1f, 1f)
        }
        // 透视投影矩阵
        Matrix.frustumM(projection, 0, -radio, radio, -1f, 1f, 1f, 10f)
//        Matrix.setLookAtM(lookupMatrix, 0, 0f, .5f
//                , 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)


    }


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        val position = GLES30.glGetAttribLocation(mProgram, "position")
        val matrix = GLES30.glGetUniformLocation(mProgram, "uMatrix")

        val matrixResult = FloatArray(16)
//        // (摄像机矩阵 * 变换矩阵) * 投影矩阵
//        Matrix.multiplyMM(matrixResult, 0, lookupMatrix, 0, uMatrix, 0)
//        Matrix.multiplyMM(matrixResult, 0, matrixResult, 0, projection, 0)

        Log.d(tag, "matrixResult : ${Arrays.toString(matrixResult)}")
        GLES30.glVertexAttribPointer(position, 3, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer)
        GLES30.glUniformMatrix4fv(matrix, 1, false, projection, 0)
        GLES30.glEnableVertexAttribArray(position)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vertexArray.size / 3)
    }
}