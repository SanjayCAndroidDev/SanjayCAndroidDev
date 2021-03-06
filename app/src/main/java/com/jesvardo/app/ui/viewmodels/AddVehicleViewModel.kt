package com.jesvardo.app.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseViewModel
import com.jesvardo.app.network.model.ModelResponseGetVehicleType
import com.jesvardo.app.network.model.ModelResponseVehicleModel
import com.jesvardo.app.network.model.ModelresponseVehicleMakes
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

class AddVehicleViewModel : BaseViewModel() {

    //Disposables
    private lateinit var subscription: Disposable

    var strEmail = MutableLiveData<String>("")
    var strError = MutableLiveData<String>("")
    var vehicleTypeSuccess = MutableLiveData<Boolean>(false)
    var vehicleModelSuccess = MutableLiveData<Boolean>(false)
    var vehicleMakesSuccess = MutableLiveData<Boolean>(false)

    lateinit var listVehicleType: ArrayList<ModelResponseGetVehicleType>
    lateinit var listVehicleModel: ArrayList<ModelResponseVehicleModel>
    var listVehicleMakes: MutableLiveData<ModelresponseVehicleMakes> = MutableLiveData()


    fun getVehicleType() {

        subscription = postApi.getVehicleType()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                listVehicleType = it
                vehicleTypeSuccess.value = true
            }, {

                println("listGetVehicleType HttpException...$it")

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

    fun getVehicleModel(strID: String) {
        var strToken = "Bearer " + appPreferences.getAppPrefString(AppConstants.PREF_USER_TOKEN)
        subscription = postApi.getVehicleModel(strToken, strID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                listVehicleModel = it
                vehicleModelSuccess.value = true
            }, {

                println("listGetVehicleType HttpException...$it")

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

    fun getVehicleMakes(strID: String) {
        var strToken = "Bearer " + appPreferences.getAppPrefString(AppConstants.PREF_USER_TOKEN)
        subscription = postApi.getVehicleMakes(strToken, strID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                listVehicleMakes.value = it
            }, {

                println("listGetVehicleType HttpException...$it")

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