package dev.tiagooliveira.tripguru.auth

import android.content.SharedPreferences
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): AuthRepository {

    override suspend fun signUp(username: String, email: String, password: String): AuthResult<Unit> {
        return try {
            api.signUp(
                request = AuthSignupRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            signIn(username, password)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.signIn(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("access", response.access)
                .apply()
            prefs.edit()
                .putString("refresh", response.refresh)
                .apply()

            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 2001) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("access", null) ?: return AuthResult.Unauthorized()
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
           AuthResult.UnknownError()
        }
    }
}