package com.jesvardo.app.network.model

data class ModelResponseForceUpdate(
    var success: String,
    var data: ModelResponseForceUpdateData
)

data class ModelResponseForceUpdateData(
    var update: String,
    var flag: String
)