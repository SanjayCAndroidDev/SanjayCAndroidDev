package com.jesvardo.app.network.model


data class ModelResponseVehicleListImages(
    var id: String,
    var primary: Boolean,
    var url: String,
    var position: Int,
    var description: String,
    var bucket_id: String
)
