package com.amrdeveloper.askme.events

data class LoginSuccessEvent(
    val email: String,
    val password: String,
    val token: String
)