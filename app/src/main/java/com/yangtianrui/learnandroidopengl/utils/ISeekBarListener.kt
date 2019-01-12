package com.yangtianrui.learnandroidopengl.utils

import android.view.View
import android.widget.SeekBar
import java.io.Serializable

/**
 * Copyright (C) 2018 The Learn Android OpenGL Project.
 *
 * @author yangtianrui
 * @date 2018/12/26
 */
interface ISeekBarListener : Serializable {

    fun onProgressChanged(seekBar: SeekBar?, view: View?, progress: Int)

}