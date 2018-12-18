package com.yangtianrui.learnandroidopengl.glviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.yangtianrui.learnandroidopengl.utils.IViewFactory

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
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

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun getVertexShader(): String = ""
    override fun getFragmentShader(): String = ""
}