package com.jesvardo.app.network.model


data class ModelResponseVehicleType(
    var id: Int,
    var label: String,
    var style: String,
    var type: String,
    var created_at: String,
    var updated_at: String,
    var suggested_security_deposit: Int,
    var suggested_nightly_rate: Int,
    var suggested_weekly_rate_min: Int,
    var suggested_weekly_rate_max: Int,

    var vehicle_makes: ArrayList<ModelresponseVehicleMakes>
)