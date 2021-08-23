package com.jesvardo.app.utils

import android.content.Context
import android.content.Intent
import com.jesvardo.app.ui.dialogs.ActivityDialog

class AppDialogUtils(var context: Context) {

    companion object {
        fun newInstance(context: Context): AppDialogUtils {
            return AppDialogUtils(context)
        }
    }

    fun startDefaultDialog(
        strTitle: String,
        strPositiveText: String,
        strNegativeText: String
    ) {
        val i = Intent(context, ActivityDialog::class.java)
        i.putExtra("strTitle", strTitle)
        i.putExtra("strPositiveText", strPositiveText)
        i.putExtra("strNegativeText", strNegativeText)
        context.startActivity(i)
    }

}