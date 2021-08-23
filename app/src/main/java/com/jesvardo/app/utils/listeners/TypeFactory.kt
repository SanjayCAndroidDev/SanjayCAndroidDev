package com.jesvardo.app.utils.listeners

import android.content.Context
import android.graphics.Typeface

class TypeFactory(context: Context) {

    private val REGULAR = "Poppins-Regular.otf"
    private val MEDIUM = "Poppins-Medium.otf"
    private val SEMIBOLD = "Poppins-SemiBold.otf"

    var regular: Typeface
    var medium: Typeface
    var semibold: Typeface

    init {
        regular = Typeface.createFromAsset(context.assets, REGULAR)
        medium = Typeface.createFromAsset(context.assets, MEDIUM)
        semibold = Typeface.createFromAsset(context.assets, SEMIBOLD)
    }
}
