package com.jesvardo.app.network

import com.jesvardo.app.network.model.*
import com.jesvardo.app.utils.AppConstants
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * The interface which provides methods to get result of webservices
 */

interface PostApi {

    @POST(AppConstants.URL_USER_LOGIN)
    fun normalUserLogin(@Body requestBody: RequestBody): Observable<ModelResponseLogin>

    @FormUrlEncoded
    @POST(AppConstants.URL_FORGOT_PASSWORD)
    fun doForgotPassword(@FieldMap hashMap: HashMap<String, String>): Observable<ModelResponseForgotPassword>

    @POST(AppConstants.URL_REGISTER)
    fun registerUser(@Body requestBody: RequestBody): Observable<ModelResponseForgotPassword>

    @FormUrlEncoded
    @POST(AppConstants.URL_FORCE_UPDATE)
    fun forceUpdate(@FieldMap hashMap: HashMap<String, String>): Observable<ModelResponseForceUpdate>

    @Headers("Content-Type: application/json")
    @POST(AppConstants.URL_SOCIAL_LOGIN)
    fun socialLogin(@Body mRequestBody: RequestBody): Observable<ModelResponseLogin>

    @GET(AppConstants.URL_GET_VEHICLE_TYPE)
    fun getVehicleType(): Observable<ArrayList<ModelResponseGetVehicleType>>


    @GET(AppConstants.URL_GET_VEHICLE_MODELS)
    fun getVehicleModel(@Header("Authorization") strToken: String, @Path("id") strUserID: String?): Observable<ArrayList<ModelResponseVehicleModel>>

    @GET(AppConstants.URL_GET_VEHICLE_MAKE)
    fun getVehicleMakes(@Header("Authorization") strToken: String, @Path("id") strUserID: String?): Observable<ModelresponseVehicleMakes>

    @GET(AppConstants.URL_GET_VEHICLE_TYPE)
    fun getVehicleType(@Query("style") strType: String): Observable<ArrayList<ModelResponseGetVehicleType>>

    @GET(AppConstants.URL_GET_VEHICLE_LIST)
    fun getVehicleList(@QueryMap hashMap: HashMap<String, String>): Observable<ArrayList<ModelResponseVehicleList>>

    @GET(AppConstants.URL_GET_VEHICLE_DETAILS)
    fun getVehicleDetails(@Path("id") strUserID: String?): Observable<ModelResponseVehicleList>

    @GET(AppConstants.URL_GET_VEHICLE_MAKE)
    fun getVehicleMake(@Path("id") strUserID: String?): Observable<ArrayList<ModelResponseGetVehicleType>>

    @FormUrlEncoded
    @POST(AppConstants.URL_LOGOUT)
    fun doUserLogout(@Path("id") strUserID: String?): Observable<ModelResponseLogout>

    @GET(AppConstants.URL_GET_ADDRESS)
    fun getAddress(@Header("Authorization") strToken: String): Observable<ArrayList<ModelResponseAddress>>

    @GET(AppConstants.URL_GET_BOOKINGS)
    fun getMyTrips(@Header("Authorization") strToken: String): Observable<ArrayList<ModelResponseMyBookingList>>

    @GET(AppConstants.URL_GET_BOOKING_DETAILS)
    fun getMyTripDetails(@Header("Authorization") strToken: String, @Path("id") strUserID: String?): Observable<ModelResponseMyBookingList>


    @GET(AppConstants.URL_GET_USER_DETAILS)
    fun getUserDetails(@Header("Authorization") strToken: String, @Path("id") strUserID: String?): Observable<ModelResponseUserDeatils>
}