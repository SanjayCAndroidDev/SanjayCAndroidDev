package com.jesvardo.app.network.model

data class ModelResponseUserDeatils(
    var username: String,
    var id: Int,
    var first_name: String,
    var last_name: String,
    var email: String,
    var provider: String,
    var confirmed: Boolean,
    var phone_number: String,
    var gender: String,
    var dob: String,
    var avatar_url: String,
    var role: ModelResponseRole,
    var stripe_customer_id: String,
    var phone_country_code: String

)

