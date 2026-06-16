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

data class PagedUsersResponse(
    val users: List<UserResponse>,
    val page: Int,
    val limit: Int,
    val total: Long,
)
