package com.juanmaav.blueprint.core.usecase

import com.juanmaav.blueprint.core.domain.User
import com.juanmaav.blueprint.infra.persistence.InMemoryUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ListUsersUseCaseTest {
    private val repository = InMemoryUserRepository()
    private val useCase = ListUsersUseCase(repository)

    @Test
    fun `should return empty page when there are no users`() =
        runTest {
            val page = useCase.execute(1, 10)

            assertTrue(page.items.isEmpty())
            assertEquals(0, page.total)
        }

    @Test
    fun `should paginate results`() =
        runTest {
            repeat(3) { i -> repository.save(User(name = "User $i", email = "u$i@test.com", age = 20 + i)) }

            val firstPage = useCase.execute(1, 2)
            assertEquals(2, firstPage.items.size)
            assertEquals(3, firstPage.total)

            val secondPage = useCase.execute(2, 2)
            assertEquals(1, secondPage.items.size)
            assertEquals(3, secondPage.total)
        }
}
