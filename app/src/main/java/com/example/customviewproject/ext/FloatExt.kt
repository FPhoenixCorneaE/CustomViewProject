package com.example.customviewproject.ext.szj

import android.content.res.Resources
import android.util.TypedValue

/**
 *
 * @ClassName: FloatExt
 * @Author: 史大拿
 * @CreateDate: 8/5/22$ 2:37 PM$
 * TODO
 */

val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this,
        Resources.getSystem().displayMetrics)

