package com.jesvardo.app.network.model

data class ModelResponseVehicleListReview(
    var id: String,
    var first_name: String,
    var last_name: String,
    var avatar_url: String,
    var text: String,
    var rating: Int,
    var listing: String,
    var user: String,
    var created_by: String,
    var updated_by: String
)