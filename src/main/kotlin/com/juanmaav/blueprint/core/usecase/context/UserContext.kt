package com.juanmaav.blueprint.core.usecase.context

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.platform.context.FlowContext

class UserContext(
    val user: User,
    var persistedUser: User? = null,
) : FlowContext()
