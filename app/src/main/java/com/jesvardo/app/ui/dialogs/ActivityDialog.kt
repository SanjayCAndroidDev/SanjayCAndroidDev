package com.jesvardo.app.ui.dialogs

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jesvardo.app.R
import com.jesvardo.app.base.MyApplication
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.dialog_default.*

class ActivityDialog : AppCompatActivity() {

    private var strTitle: String? = ""
    private var strPositiveText: String? = ""
    private var strNegativeText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_default)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setFinishOnTouchOutside(true)

        if (intent.hasExtra("strTitle")) {
            strTitle = intent.getStringExtra("strTitle")
        }

        if (intent.hasExtra("strPositiveText")) {
            strPositiveText = intent.getStringExtra("strPositiveText")
        }

        if (intent.hasExtra("strNegativeText")) {
            strNegativeText = intent.getStringExtra("strNegativeText")
        }

        dialog_default_textview_title.text = strTitle
        dialog_default_textview_positive.text = strPositiveText
        dialog_default_textview_negative.text = strNegativeText

        dialog_default_textview_positive.setSafeOnClickListener {
            onBackPressed()
            MyApplication.dialogDefaultButtonClick.value = 1
        }

        dialog_default_textview_negative.setSafeOnClickListener {
            onBackPressed()
            MyApplication.dialogDefaultButtonClick.value = 2
        }

    }

}