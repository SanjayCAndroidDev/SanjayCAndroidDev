package com.jesvardo.app.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class AppSocialLoginUtils(var application: Application) {

    companion object {
        fun newInstance(application: Application): AppSocialLoginUtils {
            return AppSocialLoginUtils(application)
        }

        var googleSignIn = 0
    }

    //Facebook
    private lateinit var callbackManager: CallbackManager

    //Google
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    fun initFacebook(activity: Activity, callBack: FacebookCallback<LoginResult?>) {
        LoginManager.getInstance().logOut()

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, callBack)

        LoginManager.getInstance()
            .logInWithReadPermissions(activity, listOf("email", "public_profile", "user_friends"))
    }

    fun getCallbackManager(): CallbackManager {
        return callbackManager
    }

    fun initGoogleSignIn(activity: Activity) {
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

        // Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        try {
            if (account!!.id != "") {
                mGoogleSignInClient.signOut()
            }
        } catch (E: KotlinNullPointerException) {

        }

        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, googleSignIn)
    }

    fun printHashKey(pContext: Context) {
        try {
            val info: PackageInfo = pContext.packageManager
                .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("LoginActivity", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("LoginActivity", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("LoginActivity", "printHashKey()", e)
        }
    }

}