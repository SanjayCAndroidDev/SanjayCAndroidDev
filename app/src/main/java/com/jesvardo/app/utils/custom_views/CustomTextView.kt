package com.jesvardo.app.utils.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jesvardo.app.R
import com.jesvardo.app.base.MyApplication

class CustomTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {
    private var typefaceType = 0

    init {
        val array: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTextView,
            0, 0
        )

        typefaceType = try {
            array.getInteger(R.styleable.CustomTextView_font_name, 0)
        } finally {
            array.recycle()
        }

        if (!isInEditMode) {
            setTextIsSelectable(false)
            typeface = MyApplication.getTypeFace(typefaceType)
        }
    }
}
