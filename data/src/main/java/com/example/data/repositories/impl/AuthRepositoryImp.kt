package com.example.data.repositories.impl

import com.example.data.repositories.intrefaces.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class AuthRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): SignUpResponse {
        return try {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                runBlocking { com.example.common.ResponseResult.Success(true) }
            }.await()
            com.example.common.ResponseResult.Success(true)
        } catch (e: Exception) {
            com.example.common.ResponseResult.Failure(e)
        }
    }

    override suspend fun sendEmailVerification(): SendEmailVerificationResponse {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            com.example.common.ResponseResult.Success(true)
        } catch (e: Exception) {
            com.example.common.ResponseResult.Failure(e)
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInResponse {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            com.example.common.ResponseResult.Success(true)
        } catch (e: Exception) {
            com.example.common.ResponseResult.Failure(e)
        }
    }

    override suspend fun reloadFirebaseUser(): ReloadUserResponse {
        return try {
            auth.currentUser?.reload()?.await()
            com.example.common.ResponseResult.Success(true)
        } catch (e: Exception) {
            com.example.common.ResponseResult.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse {
        return try {
            auth.sendPasswordResetEmail(email).await()
            com.example.common.ResponseResult.Success(true)
        } catch (e: Exception) {
            com.example.common.ResponseResult.Failure(e)
        }
    }

    override fun signOut() = auth.signOut()


    override suspend fun revokeAccess(): RevokeAccessResponse {
        return try {
            auth.currentUser?.delete()?.await()
            com.example.common.ResponseResult.Success(true)
        } catch (e: Exception) {
            com.example.common.ResponseResult.Failure(e)
        }
    }

    override fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

}

