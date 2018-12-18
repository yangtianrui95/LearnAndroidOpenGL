package com.yangtianrui.learnandroidopengl.glutils

import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2018/12/18
 */


object BufferUtils {

    fun createBuffer(floats: FloatArray): Buffer {
        return ByteBuffer
                .allocateDirect(floats.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(floats)
                .position(0)
    }

    fun createBuffer(bytes: ByteArray): Buffer {
        return ByteBuffer
                .allocateDirect(bytes.size * 4)
                .order(ByteOrder.nativeOrder())
                .put(bytes)
                .position(0)
    }
}