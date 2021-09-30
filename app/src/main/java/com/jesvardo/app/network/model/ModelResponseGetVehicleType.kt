package com.jesvardo.app.network.model

data class ModelResponseGetVehicleType(
    var id: Int,
    var label: String,
    var type: String,
    var style: String,
    var vehicle_makes: ArrayList<ModelresponseVehicleMakes>,
    var isSeleted: Boolean
)


