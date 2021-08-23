package com.jesvardo.app.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseViewModel
import com.jesvardo.app.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class LoginViewModel : BaseViewModel() {

    //Disposables
    private lateinit var subscription: Disposable

    var strFirstname = MutableLiveData<String>("")
    var strLastname = MutableLiveData<String>("")
    var strUsername = MutableLiveData<String>("")
    var strPassword = MutableLiveData<String>("")
    var strError = MutableLiveData<String>("")
    var booleanLoginSuccess = MutableLiveData<Boolean>(false)

    fun doNormalLogin() {

        val paramObject = JSONObject()
        paramObject.put("identifier", strUsername.value.toString())
        paramObject.put("password", strPassword.value.toString())

        val mRequestBody: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), paramObject.toString())

        subscription = postApi.normalUserLogin(mRequestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_ID, it.user.id.toString())
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_TOKEN, it.jwt)
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_EMAIL, it.user.email)
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_FIRST_NAME, it.user.first_name)
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_LAST_NAME, it.user.last_name)
                booleanLoginSuccess.value = true

            }, {
                when (it) {
                    is HttpException -> {
                        try {
                            if (it.code() == AppConstants.INT_UNAUTHORIZED) {
                                val mJsonObjectMsg = JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")
                            } else {
                                val mJsonObjectMsg = JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")
                            }
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }
                    is SocketTimeoutException -> {
                        strError.value = context.resources.getString(R.string.text_time_out_msg)
                    }
                    else -> {
                        strError.value = context.resources.getString(R.string.text_server_error_msg)
                    }
                }
            })

    }


    fun doSocialLogin(strProvider: String, strAccessToken: String, strEmail: String, strFirstName: String, strLastName: String, strAvatarURL: String) {

        val mJsonObjectLogin = JSONObject()

        try {
            mJsonObjectLogin.put("provider", strProvider)
            mJsonObjectLogin.put("access_token", strAccessToken)
            mJsonObjectLogin.put("email", strEmail)
            mJsonObjectLogin.put("first_name", strFirstName)
            mJsonObjectLogin.put("last_name", strLastName)
            mJsonObjectLogin.put("avatar_url", strAvatarURL)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val mRequestBody: RequestBody = mJsonObjectLogin.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


        subscription = postApi.socialLogin(mRequestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_ID, it.user.id.toString())
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_TOKEN, it.jwt)
                appPreferences.writeSharedPreferencesString(AppConstants.PREF_USER_EMAIL, it.user.email)
                booleanLoginSuccess.value = true

            }, {
                when (it) {
                    is HttpException -> {
                        try {
                            if (it.code() == AppConstants.INT_UNAUTHORIZED) {
                                val mJsonObjectMsg = JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")
                            } else {
                                val mJsonObjectMsg = JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")
                            }
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }
                    is SocketTimeoutException -> {
                        strError.value = context.resources.getString(R.string.text_time_out_msg)
                    }
                    else -> {
                        strError.value = context.resources.getString(R.string.text_server_error_msg)
                    }
                }
            })

    }

    fun doRegisterUser() {

//        var hashMap: HashMap<String, String> = HashMap<String, String>()
//        hashMap["first_name"] = strFirstname.value.toString()
//        hashMap["last_name"] = strLastname.value.toString()
//        hashMap["email"] = strUsername.value.toString()
//        hashMap["password"] = strPassword.value.toString()

        val paramObject = JSONObject()
        paramObject.put("first_name", strFirstname.value.toString())
        paramObject.put("last_name", strLastname.value.toString())
        paramObject.put("email", strUsername.value.toString())
        paramObject.put("password", strPassword.value.toString())

        val mRequestBody: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), paramObject.toString())

        subscription = postApi.registerUser(mRequestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                booleanLoginSuccess.value = true

            }, {
                when (it) {
                    is HttpException -> {
                        try {
                            if (it.code() == AppConstants.INT_UNAUTHORIZED) {
                                val mJsonObjectMsg = JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")
                            } else {
                                val mJsonObjectMsg = JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")
                            }
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }
                    is SocketTimeoutException -> {
                        strError.value = context.resources.getString(R.string.text_time_out_msg)
                    }
                    else -> {
                        strError.value = context.resources.getString(R.string.text_server_error_msg)
                    }
                }
            })

    }

}