package com.juanmaav.blueprint.core.usecase

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.infra.persistence.InMemoryUserRepository
import com.juanmaav.platform.exception.HttpException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetUserUseCaseTest {
    private val repository = InMemoryUserRepository()
    private val useCase = GetUserUseCase(repository)

    @Test
    fun `should return user when it exists`() =
        runTest {
            val saved = repository.save(User(name = "Ada", email = "ada@test.com", age = 30))

            val result = useCase.execute(saved.id!!)

            assertEquals(saved.id, result.id)
            assertEquals("Ada", result.name)
        }

    @Test
    fun `should throw 404 when user is missing`() =
        runTest {
            val ex =
                assertThrows<HttpException> {
                    useCase.execute("does-not-exist")
                }

            assertEquals(404, ex.httpStatus)
            assertEquals("USER_NOT_FOUND", ex.code)
        }
}
