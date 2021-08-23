package com.jesvardo.app.network.model

data class ModelResponseVehicleListListingAddons(
    var id: String,
    var name: String,
    var description: String,
    var price: Float,
    var quantity: Int,
    var required: Boolean,
    var is_applicable_daily: Boolean,
    var is_paid_on_return: Boolean,
    var listing: String,
    var tax_rate: Float,
    var created_by: String,
    var updated_by: String
)

