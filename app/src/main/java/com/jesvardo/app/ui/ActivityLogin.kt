package com.jesvardo.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.base.MyApplication
import com.jesvardo.app.databinding.ActivityLoginBinding
import com.jesvardo.app.ui.viewmodels.LoginViewModel
import com.jesvardo.app.utils.AppSocialLoginUtils
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.base_activity.*


class ActivityLogin : BaseActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private var isFacebookLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = putContentView(R.layout.activity_login) as ActivityLoginBinding

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        activityLoginBinding.loginViewModel = loginViewModel

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        appPermissions.checkPermissions()

        setForgotPasswordSpan()

        activity_login_textview_login_button.setSafeOnClickListener {
            if (isValid()) {
                if (MyApplication.isInternetAvailable) {
                    loginViewModel.doNormalLogin()
                } else {
                    loginViewModel.strError.value = context.resources.getString(R.string.internet_error)
                }
            }
        }

        activity_login_textview_forgot_password.setSafeOnClickListener {
            val i = Intent(this@ActivityLogin, ActivityForgotPassword::class.java)
            startActivity(i)
        }

        activity_login_txt_skip.setSafeOnClickListener {
            val i = Intent(this@ActivityLogin, ActivityDashboard::class.java)
            finishAffinity();
            startActivity(i)
        }

        activity_login_textview_register.setSafeOnClickListener {
            val i = Intent(this@ActivityLogin, ActivityRegister::class.java)
            i.putExtra("isFromLogin", true)
            startActivity(i)
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

        loginViewModel.booleanLoginSuccess.observe(this, Observer {
            if (it) {
                val i = Intent(this@ActivityLogin, ActivityDashboard::class.java)
                startActivity(i)
                finishAffinity()
            }
        })

        activity_login_txt_back.setSafeOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun isValid(): Boolean {
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

    private fun setForgotPasswordSpan() {
        val styledString = SpannableString(resources.getString(R.string.text_login_forget_password))

        styledString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_button_background)),
            (styledString.length - 1),
            styledString.length,
            0
        )

//        activity_login_textview_forgot_password.text = styledString
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (isFacebookLogin) {
            appSocialLoginUtils.getCallbackManager().onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == AppSocialLoginUtils.googleSignIn) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this@ActivityLogin, "Login Success", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(
                "Google Login",
                "signInResult:failed code=" + e.statusCode
            )
        }
    }

}