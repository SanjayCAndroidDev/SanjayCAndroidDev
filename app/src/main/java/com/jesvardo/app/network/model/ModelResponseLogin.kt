package com.jesvardo.app.network.model

data class ModelResponseLogin(
    var jwt : String,
    var user: ModelResponseLoginData
)

data class ModelResponseLoginData(
    var username: String,
    var id: Int,
    var first_name: String,
    var last_name: String,
    var email: String,
    var provider: String,
    var confirmed: Boolean,
    var role: ModelResponseRole

)

data class ModelResponseRole(
    var id: Int,
    var name: String,
    var description: String,
    var type: String
)
