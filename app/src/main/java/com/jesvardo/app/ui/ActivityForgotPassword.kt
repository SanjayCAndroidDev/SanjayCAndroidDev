package com.jesvardo.app.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.base.MyApplication
import com.jesvardo.app.databinding.ActivityForgotPasswordBinding
import com.jesvardo.app.ui.viewmodels.ForgotPasswordViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.base_activity.*

class ActivityForgotPassword : BaseActivity() {

    private lateinit var activityForgotPasswordBinding: ActivityForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityForgotPasswordBinding = putContentView(R.layout.activity_forgot_password) as ActivityForgotPasswordBinding

        forgotPasswordViewModel =
            ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        activityForgotPasswordBinding.forgotPasswordViewModel = forgotPasswordViewModel

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activity_forgot_password_textview_send.setSafeOnClickListener {
            if (isValid()) {
                if (MyApplication.isInternetAvailable) {
                    forgotPasswordViewModel.doForgotPassword()
                } else {
                    forgotPasswordViewModel.strError.value =
                        context.resources.getString(R.string.internet_error)
                }
            }
        }

        forgotPasswordViewModel.strError.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        forgotPasswordViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })

        activity_forgot_password_txt_back.setSafeOnClickListener {
            onBackPressed()
        }

        forgotPasswordViewModel.booleanForgotPasswordSuccess.observe(this, Observer {
            if (it) {
                onBackPressed()
            }
        })

    }

    private fun isValid(): Boolean {
        if (forgotPasswordViewModel.strEmail.value == "") {
            activity_forgot_password_edittext_username.error =
                resources.getString(R.string.text_forgot_password_empty_email)
            return false
        }
        if (!appUtils.isEmailValid(forgotPasswordViewModel.strEmail.value.toString())) {
            activity_forgot_password_edittext_username.error =
                resources.getString(R.string.text_forgot_password_invalid_email)
            return false
        }

        return true
    }

}