package com.jesvardo.app.network.model

data class ModelResponseVehicleListListingAvailability(
    var id: String,
    var is_available: Boolean,
    var unavailable_reason: String,
    var listing: String,
    var date: String,
    var published_at: String,
    var created_by: String,
    var updated_by: String
)

