package com.jesvardo.app.network.model

data class ModelResponseVehicleModel(
    var id: Int,
    var name: String,
    var active: Boolean,
    var created_at: String,
    var updated_at: String,
    var vehicle_make: Int
)