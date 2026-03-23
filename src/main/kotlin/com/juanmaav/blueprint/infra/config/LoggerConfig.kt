package com.juanmaav.blueprint.infra.config

import com.juanmaav.platform.logger.JsonStructuredLogger
import com.juanmaav.platform.logger.StructuredLogger
import com.juanmaav.platform.logger.TraceInfo
import com.juanmaav.platform.logger.TraceProvider
import io.opentelemetry.api.trace.Span
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class LoggerConfig {
    @ConfigProperty(name = "quarkus.application.name", defaultValue = "kotlin-quarkus-blueprint")
    lateinit var serviceName: String

    @Produces
    @ApplicationScoped
    fun traceProvider(): TraceProvider =
        TraceProvider {
            val spanContext = Span.current().spanContext
            if (spanContext.isValid) {
                TraceInfo(
                    traceId = spanContext.traceId,
                    spanId = spanContext.spanId,
                )
            } else {
                null
            }
        }

    @Produces
    @ApplicationScoped
    fun structuredLogger(traceProvider: TraceProvider): StructuredLogger = JsonStructuredLogger(serviceName, traceProvider)
}
