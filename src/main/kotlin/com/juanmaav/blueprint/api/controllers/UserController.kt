package com.juanmaav.blueprint.api.controllers

import com.juanmaav.blueprint.api.dto.CreateUserRequest
import com.juanmaav.blueprint.api.dto.PagedUsersResponse
import com.juanmaav.blueprint.api.dto.UserResponse
import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.port.input.CreateUserPort
import com.juanmaav.blueprint.core.port.input.GetUserPort
import com.juanmaav.blueprint.core.port.input.ListUsersPort
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
open class UserController
    @Inject
    constructor(
        private val createUserPort: CreateUserPort,
        private val getUserPort: GetUserPort,
        private val listUsersPort: ListUsersPort,
    ) {
        @POST
        suspend fun create(request: CreateUserRequest): UserResponse {
            val user =
                User(
                    name = request.name,
                    email = request.email,
                    age = request.age,
                )

            return createUserPort.execute(user).toResponse()
        }

        @GET
        @Path("/{id}")
        suspend fun getById(
            @PathParam("id") id: String,
        ): UserResponse = getUserPort.execute(id).toResponse()

        @GET
        suspend fun list(
            @QueryParam("page") @DefaultValue("1") page: Int,
            @QueryParam("limit") @DefaultValue("10") limit: Int,
        ): PagedUsersResponse {
            val result = listUsersPort.execute(page, limit)
            return PagedUsersResponse(
                users = result.items.map { it.toResponse() },
                page = result.page,
                limit = result.limit,
                total = result.total,
            )
        }
    }

private fun User.toResponse(): UserResponse =
    UserResponse(
        id = id!!,
        name = name,
        email = email,
    )
