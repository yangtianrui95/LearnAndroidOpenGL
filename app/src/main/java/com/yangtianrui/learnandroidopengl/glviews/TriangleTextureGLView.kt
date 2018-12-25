package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yangtianrui.learnandroidopengl.R
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
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

    private var mTextureId: Int = -1
    private val mTextureBuffer = BufferUtils.createBuffer(floatArrayOf(
            .5f, 0f,
            0f, 1f,
            1f, 1f
    ))

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        initTexture()
    }

    private fun initTexture() {
        val textureId = IntArray(1)
        GLES30.glGenTextures(1, textureId, 0)
        mTextureId = textureId[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId)
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        // 将Bitmap加载进显存
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.wall)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        Log.d(tag, "texture id = $mTextureId")
    }


    override fun drawTriangle() {
        val textureCoord = GLES30.glGetAttribLocation(mProgram, "aTextureCoord")
        GLES30.glVertexAttribPointer(textureCoord, 2, GLES30.GL_FLOAT, false, 2 * 4, mTextureBuffer)
        GLES30.glEnableVertexAttribArray(textureCoord)
        GLES30.glActiveTexture(mTextureId)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId)
        super.drawTriangle()
    }


}