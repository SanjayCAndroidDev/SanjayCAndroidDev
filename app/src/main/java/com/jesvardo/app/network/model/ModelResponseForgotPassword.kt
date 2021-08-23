package com.jesvardo.app.network.model

data class ModelResponseForgotPassword(
    var ok: String,
    var message: String,
    var data: ModelResponseLoginData
)