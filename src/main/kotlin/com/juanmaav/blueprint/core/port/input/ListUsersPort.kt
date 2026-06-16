package com.juanmaav.blueprint.core.port.input

import com.juanmaav.blueprint.core.domain.Page
import com.juanmaav.blueprint.core.domain.User

interface ListUsersPort {
    suspend fun execute(
        page: Int,
        limit: Int,
    ): Page<User>
}
