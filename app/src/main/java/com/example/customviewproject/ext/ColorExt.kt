package com.example.customviewproject.ext

import android.graphics.Color
import androidx.annotation.IntRange

/**
 *
 * @ClassName: ColorExt
 * @Author: 史大拿
 * @CreateDate: 8/8/22$ 4:22 PM$
 * TODO
 */
fun Color.red(@IntRange(from = 0, to = 255) alpha: Int = 255) {
    Color.argb(alpha, 255, 0, 0)
}