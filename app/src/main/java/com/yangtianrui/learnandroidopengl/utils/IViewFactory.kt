package com.yangtianrui.learnandroidopengl.utils

import android.content.Context
import android.view.View
import java.io.Serializable

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 * <p>
 *
 * @author yangtianrui
 * @date 2018/12/18
 */
interface IViewFactory : Serializable {

    fun create(context: Context): View
}