package com.jesvardo.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.base.MyApplication
import com.jesvardo.app.databinding.ActivityRegisterBinding
import com.jesvardo.app.ui.viewmodels.LoginViewModel
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.base_activity.*

class ActivityRegister : BaseActivity() {

    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private lateinit var loginViewModel: LoginViewModel

    var isFromLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding =
            putContentView(R.layout.activity_register) as ActivityRegisterBinding

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        activityRegisterBinding.loginViewModel = loginViewModel

        isFromLogin = intent.getBooleanExtra("isFromLogin", false)

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activity_register_txt_back.setSafeOnClickListener {
            onBackPressed()
        }

        activity_register_txt_continue.setSafeOnClickListener {
            if (isValid()) {
                if (MyApplication.isInternetAvailable) {
                    loginViewModel.doRegisterUser()
                } else {
                    loginViewModel.strError.value =
                        context.resources.getString(R.string.internet_error)
                }
            }
        }

        loginViewModel.booleanLoginSuccess.observe(this, Observer {
            if (it) {
                onBackPressed()
            }
        })

        activity_register_txt_login.setSafeOnClickListener {
            val i = Intent(this@ActivityRegister, ActivityLogin::class.java)
            startActivity(i)
            if (isFromLogin)
                finish()
        }

        loginViewModel.strError.observe(this, Observer {
            if (it != "") {
                showMessage(it)
            }
        })

        loginViewModel.isShowProgress.observe(this, Observer {
            if (it != null) {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun isValid(): Boolean {

        if (loginViewModel.strFirstname.value.toString().trim() == "") {
            loginViewModel.strError.value =
                resources.getString(R.string.text_login_empty_first_name)
            return false
        }

        if (loginViewModel.strLastname.value.toString().trim() == "") {
            loginViewModel.strError.value =
                resources.getString(R.string.text_login_empty_last_name)
            return false
        }


        if (loginViewModel.strUsername.value.toString().trim() == "") {
            loginViewModel.strError.value =
                resources.getString(R.string.text_login_empty_email)
            return false
        }
        if (!appUtils.isEmailValid(loginViewModel.strUsername.value.toString().trim())) {
            loginViewModel.strError.value =
                resources.getString(R.string.text_login_invalid_email)
            return false
        }
        if (loginViewModel.strPassword.value.toString().trim() == "") {
            loginViewModel.strError.value =
                resources.getString(R.string.text_login_empty_password)
            return false
        }

        return true
    }

}