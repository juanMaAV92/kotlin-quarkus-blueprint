package com.juanmaav.blueprint.core.usecase.steps

import com.juanmaav.blueprint.core.usecase.context.UserContext
import com.juanmaav.platform.flow.Step
import com.juanmaav.platform.validation.validate
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ValidationStep : Step<UserContext> {
    override suspend fun execute(context: UserContext): UserContext {
        validate(context.user) {
            check(value.name.isNotBlank()) { "Name is required" }
            check(value.email.contains("@")) { "Invalid email format" }
            check(value.age >= 18) { "User must be at least 18 years old" }
        }
        return context
    }
}
