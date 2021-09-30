package com.jesvardo.app.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.jesvardo.app.R
import com.jesvardo.app.base.BaseViewModel
import com.jesvardo.app.network.model.ModelResponseGetVehicleType
import com.jesvardo.app.network.model.ModelResponseVehicleList
import com.jesvardo.app.network.model.ModelResponseVehicleType
import com.jesvardo.app.network.model.ModelresponseVehicleMakes
import com.jesvardo.app.ui.ActivityVehicleDetails
import com.jesvardo.app.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class DashboardViewModel : BaseViewModel() {

    //Disposables
    private lateinit var subscription: Disposable

    var strError = MutableLiveData<String>("")
    var booleanAlreadyLoginWithOtherDevice = MutableLiveData<Boolean>(false)

    var vehicleTypeDrivableSuccess = MutableLiveData<Boolean>(false)
    var vehicleTypeTowableSuccess = MutableLiveData<Boolean>(false)
    var vehicleDetailsSuccess = MutableLiveData<Boolean>(false)
    var vehicleListSuccess = MutableLiveData<Boolean>(false)

    var vehicleDetails = MutableLiveData<ModelResponseVehicleList>()

    lateinit var listVehicleTypeDrivable: ArrayList<ModelResponseGetVehicleType>
    lateinit var listVehicleTypeTowable: ArrayList<ModelResponseGetVehicleType>
    lateinit var listVehicle: ArrayList<ModelResponseVehicleList>


    fun getVehicleList(hashMap: HashMap<String, String>) {

        subscription = postApi.getVehicleList(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                listVehicle = it
                vehicleListSuccess.value = true
                println("Ok subscribe...")
            }, {

                println("Ok HttpException...${it.toString()}")
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

    fun getVehicleType(strType: String) {

        subscription = postApi.getVehicleType(strType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
//                isShowProgress.value = true
            }
            .doOnTerminate {
//                isShowProgress.value = false
            }
            .subscribe({
                if(strType == "drivable") {
                    listVehicleTypeDrivable = it
                    vehicleTypeDrivableSuccess.value = true
                } else {
                    listVehicleTypeTowable = it
                    vehicleTypeTowableSuccess.value = true
                }

            }, {
                println("Ok Sanjay....."+it.toString())
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


    fun getVehicleDetails(id: String) {

        subscription = postApi.getVehicleDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowProgress.value = true
            }
            .doOnTerminate {
                isShowProgress.value = false
            }
            .subscribe({
                vehicleDetails.value = it
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