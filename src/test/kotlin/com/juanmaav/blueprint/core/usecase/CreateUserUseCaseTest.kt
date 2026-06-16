package com.juanmaav.blueprint.core.usecase

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.usecase.steps.AuditLogStep
import com.juanmaav.blueprint.core.usecase.steps.IndexSearchStep
import com.juanmaav.blueprint.core.usecase.steps.PersistUserStep
import com.juanmaav.blueprint.core.usecase.steps.ValidationStep
import com.juanmaav.blueprint.core.usecase.steps.WelcomeEmailStep
import com.juanmaav.blueprint.infra.persistence.InMemoryUserRepository
import com.juanmaav.platform.exception.PlatformException
import com.juanmaav.platform.logger.JsonStructuredLogger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateUserUseCaseTest {
    private val logger = JsonStructuredLogger("test")
    private val useCase =
        CreateUserUseCase(
            logger = logger,
            validationStep = ValidationStep(),
            persistUserStep = PersistUserStep(logger, InMemoryUserRepository()),
            auditLogStep = AuditLogStep(logger),
            indexSearchStep = IndexSearchStep(logger),
            welcomeEmailStep = WelcomeEmailStep(logger),
        )

    @Test
    fun `should create user successfully`() =
        runTest {
            val user = User(name = "Juan", email = "juan@test.com", age = 25)

            val result = useCase.execute(user)

            assertNotNull(result.id)
            assertEquals("Juan", result.name)
            assertEquals("juan@test.com", result.email)
        }

    @Test
    fun `should fail when validation fails and not persist`() =
        runTest {
            val user = User(name = "", email = "invalid", age = 15)

            val ex =
                assertThrows<PlatformException> {
                    useCase.execute(user)
                }

            assertEquals("VALIDATION_FAILED", ex.code)
        }
}
