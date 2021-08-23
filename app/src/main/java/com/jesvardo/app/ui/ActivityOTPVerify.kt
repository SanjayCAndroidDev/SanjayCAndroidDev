package com.jesvardo.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityOtpVerifyBinding
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_otp_verify.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.activity_register_txt_back
import kotlinx.android.synthetic.main.base_activity.*

class ActivityOTPVerify : BaseActivity() {

    private lateinit var activityOTPVerifyBinding: ActivityOtpVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityOTPVerifyBinding = putContentView(R.layout.activity_otp_verify) as ActivityOtpVerifyBinding

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activity_register_txt_back.setSafeOnClickListener {
            onBackPressed()
        }

        activity_register_relative_send.setSafeOnClickListener {
            val i = Intent(this@ActivityOTPVerify, ActivityDashboard::class.java)
            finishAffinity();
            startActivity(i)
        }
    }
}