package com.juanmaav.blueprint.core.port.input

import com.juanmaav.blueprint.core.domain.User

interface GetUserPort {
    suspend fun execute(id: String): User
}
