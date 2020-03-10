package com.salesmotoris.model

object Login {
    data class LoginResponse(val meta: Meta, val data: Data)

    data class Data(val id: Int,
                    val api_token: String,
                    val username: String,
                    val email: String,
                    val level: String)
}