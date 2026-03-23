package com.juanmaav.blueprint.api.controllers

import com.juanmaav.blueprint.api.dto.CreateUserRequest
import com.juanmaav.blueprint.api.dto.UserResponse
import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.port.input.CreateUserPort
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
open class UserController
    @Inject
    constructor(private val createUserPort: CreateUserPort) {
        @POST
        suspend fun create(request: CreateUserRequest): UserResponse {
            val user =
                User(
                    name = request.name,
                    email = request.email,
                    age = request.age,
                )

            val result = createUserPort.execute(user)

            return UserResponse(
                id = result.id!!,
                name = result.name,
                email = result.email,
            )
        }
    }
