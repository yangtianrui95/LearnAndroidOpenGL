package com.yangtianrui.learnandroidopengl.glutils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.opengl.GLES30
import android.util.Log
import java.io.ByteArrayOutputStream


/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2018/12/18
 */

object ShaderUtils {

    private var tag = "ShaderUtils"


    //加载制定shader的方法
    private fun loadShader(
            shaderType: Int, //shader的类型  GLES30.GL_VERTEX_SHADER(顶点)   GLES30.GL_FRAGMENT_SHADER(片元)
            source: String   //shader的脚本字符串
    ): Int {
        //创建一个新shader
        var shader = GLES30.glCreateShader(shaderType)
        //若创建成功则加载shader
        if (shader != 0) {
            //加载shader的源代码
            GLES30.glShaderSource(shader, source)
            //编译shader
            GLES30.glCompileShader(shader)
            //存放编译成功shader数量的数组
            val compiled = IntArray(1)
            //获取Shader的编译情况
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {//若编译失败则显示错误日志并删除此shader
                Log.e(tag, "Could not compile shader $shaderType:")
                Log.e(tag, GLES30.glGetShaderInfoLog(shader))
                GLES30.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    //创建shader程序的方法
    fun createProgram(vertexSource: String?, fragmentSource: String?): Int {
        if (vertexSource == null || fragmentSource == null) {
            return -1
        }
        //加载顶点着色器
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }

        //加载片元着色器
        val pixelShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelShader == 0) {
            return 0
        }

        //创建程序
        var program = GLES30.glCreateProgram()
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            //向程序中加入顶点着色器
            GLES30.glAttachShader(program, vertexShader)
            checkGlError("glAttachShader")
            //向程序中加入片元着色器
            GLES30.glAttachShader(program, pixelShader)
            checkGlError("glAttachShader")
            //链接程序
            GLES30.glLinkProgram(program)
            //存放链接成功program数量的数组
            val linkStatus = IntArray(1)
            //获取program的链接情况
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES30.GL_TRUE) {
                Log.e(tag, "Could not link program: ")
                // 获取编译错误原因
                Log.e(tag, GLES30.glGetProgramInfoLog(program))
                GLES30.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    //检查每一步操作是否有错误的方法
    @SuppressLint("NewApi")
    fun checkGlError(op: String) {
        var error: Int
        do {
            error = GLES30.glGetError()
            Log.e(tag, "$op: glError $error")
        } while (error != GLES30.GL_NO_ERROR)
    }

    //从assets中加载shader内容的方法
    fun loadFromAssetsFile(fname: String, r: Resources): String? {
        var result: String? = null
        try {
            val inputStream = r.assets.open(fname)
            var ch = 0
            val baos = ByteArrayOutputStream()
            do {
                ch = inputStream.read()
                baos.write(ch)
            } while (ch != -1)
            val buff = baos.toByteArray()
            baos.close()
            inputStream.close()
            result = String(buff, Charsets.UTF_8)
            result = result.replace("\\r\\n".toRegex(), "\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}
