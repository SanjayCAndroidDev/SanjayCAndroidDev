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

class ForgotPasswordViewModel : BaseViewModel() {

    //Disposables
    private lateinit var subscription: Disposable

    var strEmail = MutableLiveData<String>("")
    var strError = MutableLiveData<String>("")
    var booleanForgotPasswordSuccess = MutableLiveData<Boolean>(false)

    fun doForgotPassword() {

        var hashMap : HashMap<String, String> = HashMap<String, String> ()
        hashMap["email"] = strEmail.value.toString()

        subscription = postApi.doForgotPassword(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                strError.value = "Please check your email and reset your password."
                booleanForgotPasswordSuccess.value = true
            }, {
                when (it) {
                    is HttpException -> {
                        try {
                            if (it.code() == AppConstants.INT_UNAUTHORIZED) {
                                val mJsonObjectMsg =
                                    JSONObject(it.response()!!.errorBody()!!.string())
                                strError.value = mJsonObjectMsg.optString("message")

                            } else {
                                val mJsonObjectMsg =
                                    JSONObject(it.response()!!.errorBody()!!.string())
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