package com.juanmaav.blueprint.api.exception

import com.juanmaav.platform.exception.HttpException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class HttpExceptionMapper : ExceptionMapper<HttpException> {
    override fun toResponse(e: HttpException): Response {
        return Response.status(e.httpStatus)
            .entity(e.toHttpErrorResponse())
            .build()
    }
}
