package com.juanmaav.blueprint.core.usecase.steps

import com.juanmaav.blueprint.core.usecase.context.UserContext
import com.juanmaav.platform.flow.Step
import com.juanmaav.platform.logger.StructuredLogger
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuditLogStep(private val logger: StructuredLogger) : Step<UserContext> {
    override suspend fun execute(context: UserContext): UserContext {
        logger.info("audit_step", "Auditing user creation", mapOf("userId" to context.persistedUser?.id))
        return context
    }
}
