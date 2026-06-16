package com.juanmaav.blueprint.core.usecase

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.port.input.GetUserPort
import com.juanmaav.blueprint.core.port.output.UserRepository
import com.juanmaav.platform.exception.HttpException
import jakarta.enterprise.context.ApplicationScoped

/**
 * Reads a single user. A simple query — no saga needed (orchestration is only
 * justified for the multi-step create flow).
 */
@ApplicationScoped
open class GetUserUseCase(
    private val repository: UserRepository,
) : GetUserPort {
    override suspend fun execute(id: String): User =
        repository.findById(id)
            ?: throw HttpException("USER_NOT_FOUND", listOf("user not found"), NOT_FOUND)

    private companion object {
        const val NOT_FOUND = 404
    }
}
