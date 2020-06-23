package moch.marcin.globetrotter.service;

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

data class Token(
    val token: String
)

data class LoginRequest(
    val userId: String
)

interface AuthService {
    @POST("login")
    fun loginAsync(@Body body: LoginRequest): Deferred<Response<Token>>
}
