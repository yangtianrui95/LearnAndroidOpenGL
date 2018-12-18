package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import com.yangtianrui.learnandroidopengl.utils.IViewFactory
import java.nio.Buffer
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * <p>
 *   平移六角星
 * @author yangtianrui
 * @date 2018/12/15
 */

class HexagramGLView : AbsPrimitiveGLView {

    class Factory : IViewFactory {
        override fun create(context: Context): View {
            return HexagramGLView(context)
        }
    }

    inner class Hexagram {
        private val angleCount = 6
        private val angle = 2 * Math.PI / angleCount;
        private val vertexList = mutableListOf<Float>()
        private val uMatrix = createEmptyMatrix()
        private val buffer: Buffer

        var touchScaleX = 0f
            set(value) {
                field = value
                requestRender()
            }

        var touchScaleY = 0f
            set(value) {
                field = value
                requestRender()
            }


        @Suppress("ConvertSecondaryConstructorToPrimary")
        constructor(maxRadius: Float, minRadius: Float, z: Float) {

            for (i in 0..angleCount) {

                val currentAngle = angle * i
                // 第一个三角形
                vertexList.add(0f)
                vertexList.add(0f)
                vertexList.add(z)

                vertexList.add((maxRadius * Math.cos(currentAngle)).toFloat())
                vertexList.add((maxRadius * Math.sin(currentAngle)).toFloat())
                vertexList.add(z)

                vertexList.add((minRadius * Math.cos((currentAngle + angle / 2))).toFloat())
                vertexList.add((minRadius * Math.sin((currentAngle + angle / 2))).toFloat())
                vertexList.add(z)


                // 第二个三角形
                vertexList.add(0f)
                vertexList.add(0f)
                vertexList.add(z)

                vertexList.add((maxRadius * Math.cos(currentAngle + angle)).toFloat())
                vertexList.add((maxRadius * Math.sin(currentAngle + angle)).toFloat())
                vertexList.add(z)

                vertexList.add((minRadius * Math.cos((currentAngle + angle / 2))).toFloat())
                vertexList.add((minRadius * Math.sin((currentAngle + angle / 2))).toFloat())
                vertexList.add(z)

            }
            buffer = BufferUtils.createBuffer(vertexList.toFloatArray())
        }


        fun drawSelf(program: Int) {
            val matrixLocation = GLES30.glGetUniformLocation(program, "uMatrix")
            val position = GLES30.glGetAttribLocation(program, "position")

            // 矩阵根据手势变换
            Matrix.setIdentityM(uMatrix, 0)
            Matrix.rotateM(uMatrix, 0, touchScaleX, 0f, 1f, 0f)
            Matrix.rotateM(uMatrix, 0, touchScaleY, 1f, 0f, 0f)

            // 计算最终矩阵
            val finalMatrix = createEmptyMatrix()
            // 摄像机矩阵*变换矩阵
            Matrix.multiplyMM(finalMatrix, 0, mCamera, 0, uMatrix, 0)
            // 投影矩阵*之前相乘的矩阵
            Matrix.multiplyMM(finalMatrix, 0, mProjection, 0, finalMatrix, 0)

            // 将数据送入GPU
            GLES30.glUniformMatrix4fv(matrixLocation, 1, false, finalMatrix, 0)
            GLES30.glVertexAttribPointer(position, 3, GLES30.GL_FLOAT, false, 3 * 4, buffer)
            GLES30.glEnableVertexAttribArray(position)

            // count = 顶点个数
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexList.size / 3)
        }
    }

    private val mHexagramList: MutableList<Hexagram> = mutableListOf()
    private var mLastX: Float = 0f
    private var mLastY: Float = 0f

    private val mProjection: FloatArray = createEmptyMatrix()
    private val mCamera: FloatArray = createEmptyMatrix()

    private fun createEmptyMatrix() = FloatArray(16)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initialHexagram()
        Matrix.setIdentityM(mProjection, 0)
    }

    private fun initialHexagram() {
        for (i in 1..5) {
            mHexagramList.add(Hexagram(.5f, .2f, i * -1f))
        }
    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        val aspect = height.toFloat() / width
        // 设置正交投影比例
        //Matrix.orthoM(mProjection, 0, -1f, 1f, -aspect, aspect, 1f, 10f)
        // 设置透视投影
        Matrix.frustumM(mProjection, 0, -1f * .4f, 1f * .4f, -aspect * .4f, aspect * .4f, 1f, 50f)

        //https://blog.csdn.net/a7178077/article/details/38014373
        // 设置摄像机位置
        Matrix.setLookAtM(mCamera, 0, 0f, 0f, 2f
                , 0f, 0f, 0f
                , 0f, 1f, 0f
        )
    }

    override fun getVertexShader() = "star_vertex.glsl"

    override fun getFragmentShader() = "pointer_frag.glsl"

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x
                mLastY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val diffX = event.x - mLastX
                val diffY = event.y - mLastY
                mHexagramList.forEach {
                    it.touchScaleX += diffX * .1f
                    it.touchScaleY += diffY * .1f
                }
                mLastX = event.x
                mLastY = event.y
            }
        }


        return true
    }


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)


        mHexagramList.forEach {
            it.drawSelf(mProgram)
        }
    }

}