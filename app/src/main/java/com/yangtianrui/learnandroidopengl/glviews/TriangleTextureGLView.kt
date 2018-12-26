package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yangtianrui.learnandroidopengl.R
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import com.yangtianrui.learnandroidopengl.glutils.MatrixUtils
import com.yangtianrui.learnandroidopengl.utils.IViewFactory
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2018/12/24
 */
class TriangleTextureGLView : TrianglesGLView {

    class Factory : IViewFactory {
        override fun create(context: Context): View {
            return TriangleTextureGLView(context)
        }
    }

    private var mWallTextureId: Int = -1
    private var mFaceTextureId: Int = -1
    private val mProjection = MatrixUtils.createIdentityMatrix()
    private val mCamera = MatrixUtils.createIdentityMatrix()
    // 为每个顶点绑定纹理坐标
    private val mTextureBuffer = BufferUtils.createBuffer(floatArrayOf(
            .5f, 0f,
            0f, 1f,
            1f, 1f
    ))

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        mWallTextureId = initTexture(R.drawable.wall, 0)
        mFaceTextureId = initTexture(R.drawable.face, 1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        initProjection(width, height)
    }

    private fun initProjection(width: Int, height: Int) {
        val ratio: Float
        if (width > height) {
            ratio = width / height.toFloat()
            Matrix.frustumM(mProjection, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
        } else {
            ratio = height / width.toFloat()
            Matrix.frustumM(mProjection, 0, -1f, 1f, -ratio, ratio, 1f, 10f)
        }
        Matrix.setLookAtM(mCamera, 0, 0f, 0f, 1.1f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    private fun initTexture(drawableResource: Int, textureIndex: Int): Int {
        val textureId = IntArray(1)
        // 生成纹理
        GLES30.glGenTextures(1, textureId, 0)
        // 在绑定纹理之前，先激活纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + textureIndex)
        // 绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE0 + textureIndex)
        // 设置纹理环绕，过滤方式
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        // 将Bitmap加载进显存
        val bitmap = BitmapFactory.decodeResource(resources, drawableResource)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        Log.d(tag, "texture id = ${textureId[0]}")
        return textureId[0]
    }


    override fun drawTriangle() {
        val textureCoord = GLES30.glGetAttribLocation(mProgram, "aTextureCoord")
        val uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "uMatrix")
        val wallTextureLocation = GLES30.glGetUniformLocation(mProgram, "sTexture")
        val faceTextureLocation = GLES30.glGetUniformLocation(mProgram, "sTexture2")

        val result = MatrixUtils.createIdentityMatrix()
        Matrix.multiplyMM(result, 0, mProjection, 0, mCamera, 0)
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, result, 0)
        GLES30.glVertexAttribPointer(textureCoord, 2, GLES30.GL_FLOAT, false, 2 * 4, mTextureBuffer)
        GLES30.glEnableVertexAttribArray(textureCoord)

        // 绑定多个纹理的话，需要将纹理索引传到Shader
        GLES30.glUniform1i(wallTextureLocation, 0)
        GLES30.glUniform1i(faceTextureLocation, 1)

        super.drawTriangle()
    }


}