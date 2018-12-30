package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import com.yangtianrui.learnandroidopengl.glutils.MatrixUtils
import com.yangtianrui.learnandroidopengl.utils.IViewFactory
import java.nio.Buffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2018/12/26
 */
class BallGLView : AbsPrimitiveGLView {

    class Factory : IViewFactory {
        override fun create(context: Context): View {
            return BallGLView(context)
        }
    }

    private val mRadius: Float = .8f
    private val mUMatrix: FloatArray = MatrixUtils.createIdentityMatrix()
    private val mCameraMatrix: FloatArray = MatrixUtils.createIdentityMatrix()
    private val mProjection: FloatArray = MatrixUtils.createIdentityMatrix()


    private var mVertexCount: Int = 0
    private lateinit var mVertexBuffer: Buffer

    private var mLastX: Float = 0f
    private var mLastY: Float = 0f


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initVertexArray()
    }

    private fun initVertexArray() {
        val ballVertexArrays = mutableListOf<Float>()
        // 角度切分，切分的越小，画出来的圆越完整
        val angleSpan = 10
        // 垂直切分
        for (vAngle in -90 until 90 step angleSpan) {
            // 水平方向切分
            for (hAngle in 0..360 step angleSpan) {
                val x0 = (mRadius
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.cos(Math
                        .toRadians(hAngle.toDouble()))).toFloat()
                val y0 = (mRadius
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.sin(Math
                        .toRadians(hAngle.toDouble()))).toFloat()
                val z0 = (mRadius * Math.sin(Math
                        .toRadians(vAngle.toDouble()))).toFloat()

                val x1 = (mRadius
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.cos(Math
                        .toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y1 = (mRadius
                        * Math.cos(Math.toRadians(vAngle.toDouble())) * Math.sin(Math
                        .toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z1 = (mRadius * Math.sin(Math
                        .toRadians(vAngle.toDouble()))).toFloat()

                val x2 = (mRadius
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .cos(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val y2 = (mRadius
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .sin(Math.toRadians((hAngle + angleSpan).toDouble()))).toFloat()
                val z2 = (mRadius * Math.sin(Math
                        .toRadians((vAngle + angleSpan).toDouble()))).toFloat()

                val x3 = (mRadius
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y3 = (mRadius
                        * Math.cos(Math.toRadians((vAngle + angleSpan).toDouble())) * Math
                        .sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z3 = (mRadius * Math.sin(Math
                        .toRadians((vAngle + angleSpan).toDouble()))).toFloat()


                ballVertexArrays.add(x1)
                ballVertexArrays.add(y1)
                ballVertexArrays.add(z1)
                ballVertexArrays.add(x3)
                ballVertexArrays.add(y3)
                ballVertexArrays.add(z3)
                ballVertexArrays.add(x0)
                ballVertexArrays.add(y0)
                ballVertexArrays.add(z0)

                ballVertexArrays.add(x1)
                ballVertexArrays.add(y1)
                ballVertexArrays.add(z1)
                ballVertexArrays.add(x2)
                ballVertexArrays.add(y2)
                ballVertexArrays.add(z2)
                ballVertexArrays.add(x3)
                ballVertexArrays.add(y3)
                ballVertexArrays.add(z3)
            }
        }
        mVertexCount = ballVertexArrays.size / 3
        mVertexBuffer = BufferUtils.createBuffer(ballVertexArrays.toFloatArray())
    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        val ratio: Float
        if (width > height) {
            ratio = width.toFloat() / height
            Matrix.frustumM(mProjection, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
        } else {
            ratio = height.toFloat() / width
            Matrix.frustumM(mProjection, 0, -1f, 1f, -ratio, ratio, 39f, 50f)
        }
        Matrix.setLookAtM(mCameraMatrix, 0, 0f, 0f, 40f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    override fun getVertexShader(): String = "ball_vertex.glsl"

    override fun getFragmentShader(): String = "ball_frag.glsl"

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        val positionL = GLES30.glGetAttribLocation(mProgram, "position")
        val uMatrtixL = GLES30.glGetUniformLocation(mProgram, "uMatrix")
        val uRadiusL = GLES30.glGetUniformLocation(mProgram, "uRadius")
        val result = MatrixUtils.createIdentityMatrix()

        Matrix.multiplyMM(result, 0, mCameraMatrix, 0, mUMatrix, 0)
        Matrix.multiplyMM(result, 0, mProjection, 0, result, 0)

        Log.d(tag, "positionL = $positionL, uMatrixL = $uMatrtixL, " +
                "uRadiusL = $uRadiusL , mVertexCount = $mVertexCount")
        GLES30.glVertexAttribPointer(positionL, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer)
        GLES30.glUniformMatrix4fv(uMatrtixL, 1, false, result, 0)
        GLES30.glUniform1f(uRadiusL, mRadius)
        GLES30.glEnableVertexAttribArray(positionL)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mVertexCount)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                val diffX = event.x - mLastX
                val diffY = event.y - mLastY
                Matrix.rotateM(mUMatrix, 0, diffX * .3f, 0f, 1f, 0f)
                Matrix.rotateM(mUMatrix, 0, diffY * .3f, 1f, 0f, 0f)
                requestRender()
            }
        }
        mLastX = event!!.x
        mLastY = event.y
        return true
    }

}