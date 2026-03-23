package com.juanmaav.blueprint.core.usecase.steps

import com.juanmaav.blueprint.core.usecase.context.UserContext
import com.juanmaav.platform.flow.Step
import com.juanmaav.platform.logger.StructuredLogger
import com.juanmaav.platform.retry.retry
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID

@ApplicationScoped
class PersistUserStep(private val logger: StructuredLogger) : Step<UserContext> {
    override suspend fun execute(context: UserContext): UserContext {
        // Demostración de retry para una operación "frágil"
        val id =
            retry(maxAttempts = 3, logger = logger) {
                // Simulación de persistencia
                UUID.randomUUID().toString()
            }
        context.persistedUser = context.user.copy(id = id)
        return context
    }

    override suspend fun onFailure(context: UserContext) {
        // Lógica de compensación (ej: borrar de DB si algo falló después)
    }
}
