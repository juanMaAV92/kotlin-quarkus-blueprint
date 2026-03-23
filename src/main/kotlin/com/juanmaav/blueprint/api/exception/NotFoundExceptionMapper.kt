package com.juanmaav.blueprint.api.exception

import com.juanmaav.platform.exception.ErrorResponse
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import java.time.Instant

@Provider
class NotFoundExceptionMapper : ExceptionMapper<NotFoundException> {
    override fun toResponse(e: NotFoundException): Response =
        Response.status(404)
            .entity(
                ErrorResponse(
                    code = "NOT_FOUND",
                    messages = listOf(e.message ?: "Resource not found"),
                    timestamp = Instant.now().toString(),
                ),
            )
            .build()
}
