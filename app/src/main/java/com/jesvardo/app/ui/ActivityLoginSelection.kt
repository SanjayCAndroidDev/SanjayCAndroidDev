package com.jesvardo.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseActivity
import com.jesvardo.app.databinding.ActivityLoginSelectionBinding
import com.jesvardo.app.ui.viewmodels.LoginViewModel
import com.jesvardo.app.utils.AppSocialLoginUtils
import com.jesvardo.app.utils.listeners.setSafeOnClickListener
import kotlinx.android.synthetic.main.base_activity.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ActivityLoginSelection : BaseActivity() {

    private lateinit var activityLoginSelectionBinding: ActivityLoginSelectionBinding

    private var isFacebookLogin = false

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginSelectionBinding =
            putContentView(R.layout.activity_login_selection) as ActivityLoginSelectionBinding

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        initToolbar()
        base_activity_toolbar.visibility = View.GONE

        activityLoginSelectionBinding.activityLoginSelectionTxtPhone.setSafeOnClickListener {
            val i = Intent(this@ActivityLoginSelection, ActivityLogin::class.java)
            startActivity(i)
        }

        activityLoginSelectionBinding.activityLoginSelectionTxtCreateAccount.setSafeOnClickListener {
            val i = Intent(this@ActivityLoginSelection, ActivityRegister::class.java)
            i.putExtra("isFromLogin", false)
            startActivity(i)
        }

//        getHashKey()

        activityLoginSelectionBinding.activityLoginSelectionTxtFacebook.setSafeOnClickListener {
            isFacebookLogin = true

            appSocialLoginUtils.initFacebook(this, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("Success", "Login")
                    getUserProfile(loginResult!!.accessToken, loginResult!!.accessToken.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@ActivityLoginSelection, "Login Cancel", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onError(exception: FacebookException) {
                    Log.d("onError", exception.message)
                    Toast.makeText(
                        this@ActivityLoginSelection,
                        exception.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
        }

        activityLoginSelectionBinding.activityLoginSelectionTxtGmail.setSafeOnClickListener {
            isFacebookLogin = false
            appSocialLoginUtils.initGoogleSignIn(this)
        }

        loginViewModel.booleanLoginSuccess.observe(this, Observer {
            if (it) {
                val i = Intent(this@ActivityLoginSelection, ActivityDashboard::class.java)
                startActivity(i)
                finishAffinity()
            }
        })
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {
        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }


                // Facebook First Name
                var facebookFirstName: String = ""
                if (jsonObject.has("first_name")) {
                    facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                }


                // Facebook Middle Name
                if (jsonObject.has("middle_name")) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                }


                // Facebook Last Name
                var facebookLastName: String = ""
                if (jsonObject.has("last_name")) {
                    facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                }


                // Facebook Name
                if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }


                // Facebook Profile Pic URL
                var facebookProfilePicURL: String = ""
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                             facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                }

                // Facebook Email
                var facebookEmail: String = ""
                if (jsonObject.has("email")) {
                    facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }

                loginViewModel.doSocialLogin("facebook", token!!.token, facebookEmail, facebookFirstName, facebookLastName, facebookProfilePicURL)

            }).executeAsync()
    }

    fun getHashKey() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(
                "com.jesvardo.app",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
                Toast.makeText(
                    applicationContext,
                    Base64.encodeToString(md.digest(), Base64.DEFAULT),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (isFacebookLogin) {
            appSocialLoginUtils.getCallbackManager().onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == AppSocialLoginUtils.googleSignIn) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this@ActivityLoginSelection, "Login Success", Toast.LENGTH_LONG).show()

            println("Sanjay signInResul Success=....."+account.toString())

            println("Sanjay account.familyName=....."+account.displayName)
            println("Sanjay account.email=....."+account.email)
            println("Sanjay account.id=....."+account.id)
            println("Sanjay account.photoUrl=....."+account.photoUrl)

            loginViewModel.doSocialLogin("google", account.id, account.email, account.displayName, account.displayName, account.photoUrl.path.toString())


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