package com.juanmaav.blueprint.api.exception

import com.juanmaav.platform.exception.PlatformException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class PlatformExceptionMapper : ExceptionMapper<PlatformException> {
    override fun toResponse(e: PlatformException): Response {
        return Response.status(500)
            .entity(e.toErrorResponse())
            .build()
    }
}
