package com.jesvardo.app.network.model

data class ModelResponseAddress(
    var id: Int,
    var city: String,
    var zipcode: String,
    var street_2: String,
    var created_at: String,
    var updated_at: String,
    var street_1: String,
    var state: String,
    var country: String,
    var is_billing: Boolean
)