package com.juanmaav.blueprint.api.filters

import io.opentelemetry.api.trace.Span
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider

@Provider
@ApplicationScoped
class ResponseTraceFilter : ContainerResponseFilter {
    override fun filter(
        requestContext: ContainerRequestContext,
        responseContext: ContainerResponseContext,
    ) {
        val spanContext = Span.current().spanContext
        val traceId =
            if (spanContext.isValid) {
                spanContext.traceId
            } else {
                // Fallback: Quarkus OTEL sometimes stores the traceId in the request context
                requestContext.getProperty("quarkus.otel.traceId")?.toString()
            }

        traceId?.let {
            responseContext.headers.add("X-Trace-Id", it)
        }
    }
}
