package com.jesvardo.app.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jesvardo.app.R
import com.jesvardo.app.network.PostApi
import com.jesvardo.app.network.model.ModelResponseForceUpdate
import com.jesvardo.app.utils.AppConstants
import com.jesvardo.app.utils.AppPreferences
import com.jesvardo.app.utils.listeners.GpsStatusListener
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
import javax.inject.Inject

open class BaseViewModel : ViewModel() {

    @Inject
    lateinit var postApi: PostApi

    @Inject
    lateinit var context: Application

    @Inject
    lateinit var appPreferences: AppPreferences

    //Disposables
    private lateinit var subscription: Disposable

    var gpsStatusListener: GpsStatusListener

    var modelResponseForceUpdate = MutableLiveData<ModelResponseForceUpdate>(null)
    var booleanLogoutSuccess = MutableLiveData<Boolean>(false)

    //Progress
    val isShowProgress = MutableLiveData<Boolean>(false)
    var strErrorBase = MutableLiveData<String>("")

    init {
        inject()
        gpsStatusListener = GpsStatusListener()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        MyApplication.appComponent.inject(this)
    }

    fun checkForceUpdate() {

        var hashMap : HashMap<String, String> = HashMap<String, String> ()
        hashMap["device_type"] = AppConstants.DEVICE_TYPE.toString()
        hashMap["app_version"] = MyApplication.mStringVersion.toString()

        subscription = postApi.forceUpdate(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {}
            .doOnTerminate {}
            .subscribe({
                if (it != null) {
                    if (it.success == "1") {
                        modelResponseForceUpdate.value = it
                    }
                }
            }, {
                when (it) {
                    is HttpException -> {
                        try {
                            if (it.code() == AppConstants.INT_UNAUTHORIZED) {
                                val mJsonObjectMsg =
                                    JSONObject(it.response()!!.errorBody()!!.string())
                                strErrorBase.value = mJsonObjectMsg.optString("message")

                            } else if(it.code() == AppConstants.INT_ERROR){
                                val mJsonObjectMsg =
                                    JSONObject(it.response()!!.errorBody()!!.string())
                                strErrorBase.value = mJsonObjectMsg.optString("message")
                            } else {
                                val mJsonObjectMsg =
                                    JSONObject(it.response()!!.errorBody()!!.string())
                                strErrorBase.value = mJsonObjectMsg.optString("message")
                            }
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }
                    is SocketTimeoutException -> {
                        strErrorBase.value = context.resources.getString(R.string.text_time_out_msg)
                    }
                    else -> {
                        strErrorBase.value =
                            context.resources.getString(R.string.text_server_error_msg)
                    }
                }
            })

    }

    fun doUserLogout() {
        subscription =
            postApi.doUserLogout(appPreferences.getAppPrefString(AppConstants.PREF_USER_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isShowProgress.value = true
                }
                .doOnTerminate {
                    isShowProgress.value = false
                }
                .subscribe({
                    booleanLogoutSuccess.value = true
                }, {
                    when (it) {
                        is HttpException -> {
                            try {
                                if (it.code() == AppConstants.INT_UNAUTHORIZED) {
                                    booleanLogoutSuccess.value = true
                                } else {
                                    val mJsonObjectMsg =
                                        JSONObject(it.response()!!.errorBody()!!.string())
                                    strErrorBase.value = mJsonObjectMsg.optString("message")
                                }
                            } catch (e1: IOException) {
                                e1.printStackTrace()
                            } catch (e1: JSONException) {
                                e1.printStackTrace()
                            }
                        }
                        is SocketTimeoutException -> {
                            strErrorBase.value =
                                context.resources.getString(R.string.text_time_out_msg)
                        }
                        else -> {
                            strErrorBase.value =
                                context.resources.getString(R.string.text_server_error_msg)
                        }
                    }
                })
    }

}