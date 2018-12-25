package com.yangtianrui.learnandroidopengl.glutils

import android.opengl.Matrix

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2018/12/25
 */


object MatrixUtils {

    fun createIdentityMatrix(): FloatArray {
        val matrix = FloatArray(16)
        Matrix.setIdentityM(matrix, 0)
        return matrix
    }

}