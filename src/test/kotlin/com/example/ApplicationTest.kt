package com.example

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.mongoDataSource.MongoUserDataSource
import io.ktor.server.sessions.*
import io.ktor.server.plugins.callloging.*
import org.slf4j.event.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*
import com.example.security.hashing.SHA256HashingService
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenConfig
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            val mongoPass = System.getenv("MONGO_PW")
            val db = KMongo.createClient(
                connectionString = "mongodb+srv://Poglkj:$mongoPass@cluster0.f2eypal.mongodb.net/ktor-Auth?retryWrites=true&w=majority"
            ).coroutine
                .getDatabase("ktor-Auth")
            val userDataSource = MongoUserDataSource(db)
            val tokenService = JwtTokenService()
            val tokenConfig = TokenConfig(
                issuer = environment.config.property("jwt.issuer").getString(),
                audience = environment.config.property("jwt.audience").getString(),
                expiresIn = 5 * 365L * 24L * 60L * 60L * 1000L, //years, days, hours, minutes, seconds, milliseconds
                secret = System.getenv("JWT_SECRET")
            )
            val hashingService = SHA256HashingService()
            configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}