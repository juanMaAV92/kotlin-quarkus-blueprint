package com.juanmaav.blueprint.core.usecase

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.port.input.CreateUserPort
import com.juanmaav.blueprint.core.usecase.context.UserContext
import com.juanmaav.blueprint.core.usecase.steps.AuditLogStep
import com.juanmaav.blueprint.core.usecase.steps.IndexSearchStep
import com.juanmaav.blueprint.core.usecase.steps.PersistUserStep
import com.juanmaav.blueprint.core.usecase.steps.ValidationStep
import com.juanmaav.blueprint.core.usecase.steps.WelcomeEmailStep
import com.juanmaav.platform.flow.dsl.flow
import com.juanmaav.platform.logger.StructuredLogger
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
open class CreateUserUseCase(
    private val logger: StructuredLogger,
    private val validationStep: ValidationStep,
    private val persistUserStep: PersistUserStep,
    private val auditLogStep: AuditLogStep,
    private val indexSearchStep: IndexSearchStep,
    private val welcomeEmailStep: WelcomeEmailStep,
) : CreateUserPort {
    override suspend fun execute(user: User): User {
        val context = UserContext(user)

        return flow(context, logger) {
            step(validationStep)
            step(persistUserStep)

            parallel {
                step(auditLogStep)
                step(indexSearchStep)
            }

            asyncStep(welcomeEmailStep)
        }.persistedUser!!
    }
}
