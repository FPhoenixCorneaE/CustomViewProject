package com.example.customviewproject.ext

import android.graphics.Color

/**
 *
 * @ClassName: ColorExt
 * @Author: 史大拿
 * @CreateDate: 8/8/22$ 4:22 PM$
 * TODO
 */
 val colorRandom: Int
    get() {
        return Color.argb(
            255,
            (0 until 255).random(),
            (0 until 255).random(),
            (0 until 255).random()
        )
    }


