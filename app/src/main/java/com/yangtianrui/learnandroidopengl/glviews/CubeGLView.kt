package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.yangtianrui.learnandroidopengl.glutils.BufferUtils
import com.yangtianrui.learnandroidopengl.utils.IViewFactory
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * 立方体绘制
 *
 * @author yangtianrui
 * @date 2018/12/18
 */
// todo(yangtianrui) 手势存在问题
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

    private val colorArray = floatArrayOf(
            1f, 1f, 1f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 1f, 0f
    )

    private val vertexBuffer = BufferUtils.createBuffer(vertexArray)
    private val colorBuffer = BufferUtils.createBuffer(colorArray)
    private val uMatrix = createIdentityMatrix()
    private val projection = createIdentityMatrix()
    private val lookupMatrix = createIdentityMatrix()
    private val rotateMatrix = createIdentityMatrix()

    private var mLastX: Float = 0f
    private var mLastY: Float = 0f

    private var mMatrixLocation: Int = -1
    private var mPositionLocation: Int = -1
    private var mVColorLocation: Int = -1

    private val mTouchScale: Float = .2f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun getVertexShader(): String = "cube_vertex.glsl"

    override fun getFragmentShader(): String = "cube_frag.glsl"


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        val ratio = if (width.toFloat() / height > 0) width.toFloat() / height else height.toFloat() / width
        // 正交投影的计算
        if (width > height) {
            Matrix.orthoM(projection, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(projection, 0, -1f, 1f, -ratio, ratio, -1f, 1f)
        }
        // todo(yangtianrui) 此处目前只考虑竖屏了
        // 产生透视投影矩阵
        Matrix.frustumM(projection, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
        // 产生摄像机矩阵
        //https://blog.csdn.net/jamesshaoya/article/details/54342241
        Matrix.setLookAtM(lookupMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        findLocationInShader()
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)

        // 绘制前面
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.translateM(uMatrix, 0, 0f, 0f, 1f)
        drawSquare()

        // 绘制后面
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.translateM(uMatrix, 0, 0f, 0f, -1f)
        Matrix.rotateM(uMatrix, 0, 180f, 0f, 1f, 0f)
        drawSquare()

        // 绘制上面
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.translateM(uMatrix, 0, 0f, 1f, 0f)
        Matrix.rotateM(uMatrix, 0, -90f, 1f, 0f, 0f)
        drawSquare()

        // 绘制底面
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.translateM(uMatrix, 0, 0f, -1f, 0f)
        Matrix.rotateM(uMatrix, 0, 90f, 1f, 0f, 0f)
        drawSquare()

        // 绘制右侧面
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.translateM(uMatrix, 0, 1f, 0f, 0f)
        Matrix.rotateM(uMatrix, 0, 90f, 1f, 0f, 0f)
        Matrix.rotateM(uMatrix, 0, 90f, 0f, 1f, 0f)
        drawSquare()


        // 绘制左侧面
        Matrix.setIdentityM(uMatrix, 0)
        Matrix.translateM(uMatrix, 0, -1f, 0f, 0f)
        Matrix.rotateM(uMatrix, 0, 90f, 1f, 0f, 0f)
        Matrix.rotateM(uMatrix, 0, 90f, 0f, 1f, 0f)
        drawSquare()

    }

    private fun findLocationInShader() {
        mPositionLocation = GLES30.glGetAttribLocation(mProgram, "position")
        mMatrixLocation = GLES30.glGetUniformLocation(mProgram, "uMatrix")
        mVColorLocation = GLES30.glGetAttribLocation(mProgram, "vColor")

        Log.d(tag, "findLocationInShader position = $mPositionLocation " +
                ", uMatrix = $mMatrixLocation, vColor = $mVColorLocation")
    }

    private fun drawSquare() {
        val matrixResult = FloatArray(16)

        // 计算因手势产生的变换
        Matrix.multiplyMM(matrixResult, 0, rotateMatrix, 0, uMatrix, 0)

        // 注意矩阵乘法的顺序，顺序错误无法正确变换!
        // 投影矩阵 * (摄像机矩阵 * 变换矩阵)
        //https://glumes.com/post/opengl/opengl-tutorial-projection-matrix/
        Matrix.multiplyMM(matrixResult, 0, lookupMatrix, 0, matrixResult, 0)
        Matrix.multiplyMM(matrixResult, 0, projection, 0, matrixResult, 0)
        GLES30.glVertexAttribPointer(mPositionLocation, 3, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer)
        GLES30.glUniformMatrix4fv(mMatrixLocation, 1, false, matrixResult, 0)
        GLES30.glEnableVertexAttribArray(mPositionLocation)
        GLES30.glVertexAttribPointer(mVColorLocation, 4, GLES30.GL_FLOAT, false, 4 * 4, colorBuffer)
        GLES30.glEnableVertexAttribArray(mVColorLocation)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vertexArray.size / 3)
    }

    private fun createIdentityMatrix(): FloatArray {
        val matrix = FloatArray(16)
        Matrix.setIdentityM(matrix, 0)
        return matrix
    }


    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                val diffX = event.x - mLastX
                val diffY = event.y - mLastY
                Matrix.rotateM(rotateMatrix, 0, diffX * mTouchScale, 0f, 1f, 0f)
                Matrix.rotateM(rotateMatrix, 0, diffY * mTouchScale, 1f, 0f, 0f)
                mLastX = event.x
                mLastY = event.y
                requestRender()
            }
            else -> {
                mLastX = event!!.x
                mLastY = event!!.y
            }
        }
        return true
    }
}