package com.juanmaav.blueprint.core.usecase.steps

import com.juanmaav.blueprint.core.port.output.UserRepository
import com.juanmaav.blueprint.core.usecase.context.UserContext
import com.juanmaav.platform.flow.Step
import com.juanmaav.platform.logger.StructuredLogger
import com.juanmaav.platform.retry.retry
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersistUserStep(
    private val logger: StructuredLogger,
    private val repository: UserRepository,
) : Step<UserContext> {
    override suspend fun execute(context: UserContext): UserContext {
        // retry wraps the "fragile" persistence call (transient errors are retried).
        context.persistedUser =
            retry(maxAttempts = 3, logger = logger) {
                repository.save(context.user)
            }
        return context
    }

    override suspend fun onFailure(context: UserContext) {
        // Compensation: undo the insert if a later step in the saga failed.
        context.persistedUser?.id?.let { repository.deleteById(it) }
    }
}
