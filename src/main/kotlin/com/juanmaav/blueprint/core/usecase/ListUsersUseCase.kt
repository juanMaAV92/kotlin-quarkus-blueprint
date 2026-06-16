package com.juanmaav.blueprint.core.usecase

import com.juanmaav.blueprint.core.domain.Page
import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.port.input.ListUsersPort
import com.juanmaav.blueprint.core.port.output.UserRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Lists users with pagination. A simple query — no saga needed.
 */
@ApplicationScoped
open class ListUsersUseCase(
    private val repository: UserRepository,
) : ListUsersPort {
    override suspend fun execute(
        page: Int,
        limit: Int,
    ): Page<User> = repository.findAll(page, limit)
}
