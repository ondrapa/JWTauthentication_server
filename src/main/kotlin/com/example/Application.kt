package com.example

import com.example.data.models.User
import com.example.data.mongoDataSource.MongoUserDataSource
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.security.hashing.SHA256HashingService
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
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
    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureSockets()
}