package com.juanmaav.blueprint.api.dto

data class CreateUserRequest(
    val name: String,
    val email: String,
    val age: Int,
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
)
