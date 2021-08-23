package com.jesvardo.app.network.model

data class ModelResponseGetVehicleType(
    var id: Int,
    var label: String,
    var type: String,
    var style: String,
    var suggested_security_deposit: Int,
    var suggested_nightly_rate: Int,
    var suggested_weekly_rate_min: Int,
    var suggested_weekly_rate_max: Int,
    var active: Boolean,
    var created_by: String,
    var updated_by: String,
    var vehicle_makes: ArrayList<ModelresponseVehicleMakes>,
    var isSeleted: Boolean
)


