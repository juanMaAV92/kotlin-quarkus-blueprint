package com.juanmaav.blueprint.core.port.input

import com.juanmaav.blueprint.core.domain.User

interface CreateUserPort {
    suspend fun execute(user: User): User
}
