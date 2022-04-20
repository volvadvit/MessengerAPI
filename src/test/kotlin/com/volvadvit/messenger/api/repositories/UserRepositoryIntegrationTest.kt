package com.volvadvit.messenger.api.repositories

import com.volvadvit.messenger.api.models.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestPropertySource
import javax.sql.DataSource

@DataJpaTest
//@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest @Autowired constructor(
    private val dataSource: DataSource,
    private val jdbcTemplate: JdbcTemplate,
    private val entityManager: TestEntityManager,
    private val repository: UserRepository
) {

    @Test
    fun injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull;
        assertThat(jdbcTemplate).isNotNull;
        assertThat(entityManager).isNotNull;
        assertThat(repository).isNotNull;
    }

    @Test
    fun testFindUserByUsername() {
        val user = User(username = "name")
        entityManager.persistAndFlush(user)
        val result = repository.findByUsername(user.username)
        assertThat(result === user)
    }
}