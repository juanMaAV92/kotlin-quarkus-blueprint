package com.juanmaav.blueprint.core.usecase.steps

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.core.usecase.context.UserContext
import com.juanmaav.platform.exception.PlatformException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ValidationStepTest {
    private val step = ValidationStep()

    @Test
    fun `should pass with valid user`() =
        runTest {
            val context = UserContext(User(name = "Juan", email = "juan@test.com", age = 25))

            assertDoesNotThrow {
                step.execute(context)
            }
        }

    @Test
    fun `should fail with all validation errors`() =
        runTest {
            val context = UserContext(User(name = "", email = "invalid", age = 15))

            val ex =
                assertThrows<PlatformException> {
                    step.execute(context)
                }

            assertEquals("VALIDATION_FAILED", ex.code)
            assertEquals(3, ex.messages.size)
        }
}
