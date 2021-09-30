package com.jesvardo.app.utils

class AppConstants {

    companion object {
        const val BASE_URL: String = "https://api.jesvardo.com/"
        const val PREFS_NAME: String = "Jesvardo_Preferences"

        //Predefined Data
        const val DEVICE_TYPE: Int = 1
        const val INT_UNAUTHORIZED = 400
        const val INT_ERROR = 401

        //Shared Preferences Constants
        const val PREF_USER_IS_LOGIN = "PREF_USER_IS_LOGIN"
        const val PREF_USER_ID = "PREF_USER_ID"
        const val PREF_USER_FIRST_NAME = "PREF_USER_FIRST_NAME"
        const val PREF_USER_LAST_NAME = "PREF_USER_LAST_NAME"
        const val PREF_USER_PROFILE_IMAGE = "PREF_USER_PROFILE_IMAGE"
        const val PREF_USER_COMPANY_NAME = "PREF_USER_COMPANY_NAME"
        const val PREF_USER_EMAIL = "PREF_USER_EMAIL"
        const val PREF_USER_PHONE_NO = "PREF_USER_PHONE_NO"
        const val PREF_USER_DESIGNATION = "PREF_USER_DESIGNATION"
        const val PREF_USER_TOKEN = "PREF_USER_TOKEN"
        const val PREF_DEVICE_TOKEN = "PREF_DEVICE_TOKEN"

        //WebService Endpoints
        const val URL_USER_LOGIN = "auth/local"
        const val URL_FORGOT_PASSWORD = " auth/forgot-password"
        const val URL_REGISTER = " auth/local/register"

        const val URL_USER_PROFILE = "v1/user"
        const val URL_FORCE_UPDATE = "v1/force-update"
        const val URL_SOCIAL_LOGIN = "auth/social"

        const val URL_GET_VEHICLE_TYPE = " vehicle-types?active=true"
        const val URL_GET_VEHICLE_LIST  = "listings"
        const val URL_GET_VEHICLE_MAKE = "vehicle-makes/{id} "
        const val URL_GET_VEHICLE_DETAILS = "listings/{id}"
        const val URL_GET_VEHICLE_MODELS = "vehicle-models/{id}"

        const val URL_GET_ADDRESS = " addresses"
        const val URL_GET_BOOKINGS = " bookings"
        const val URL_GET_BOOKING_DETAILS = " bookings/{id}"

        const val URL_GET_USER_DETAILS = " users/{id}"

        const val URL_LOGOUT = "v1/auth/logout/{id}"

        //Pass Data
        const val PASS_DATA = "PASS_DATA"

        //Notification Constants
        var CONST_NOTIFICATION_CONNECTION: String? = "VYZEO_BROADCAST_NOTIFICATION"
        var CONST_IS_FROM_NOTIFICATION = "savvy_isfrom_notification"

        var CONST_NOTIFICATION_TITLE = "notification_type"
        var CONST_NOTIFICATION_BODY = "notification_body"
        var CONST_NOTIFICATION_DATA = "notification_data"

    }
}