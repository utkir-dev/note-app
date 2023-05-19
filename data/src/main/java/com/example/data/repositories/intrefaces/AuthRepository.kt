package com.example.data.repositories.intrefaces

import com.example.common.ResponseResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias SignUpResponse = ResponseResult<Boolean>
typealias SendEmailVerificationResponse = ResponseResult<Boolean>
typealias SignInResponse = ResponseResult<Boolean>
typealias ReloadUserResponse = ResponseResult<Boolean>
typealias SendPasswordResetEmailResponse = ResponseResult<Boolean>
typealias RevokeAccessResponse = ResponseResult<Boolean>
typealias AuthStateResponse = StateFlow<Boolean>

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun signUpWithEmailAndPassword(email: String, password: String): SignUpResponse

    suspend fun sendEmailVerification(): SendEmailVerificationResponse

    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResponse

    suspend fun reloadFirebaseUser(): ReloadUserResponse

    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse

    fun signOut()

    suspend fun revokeAccess(): RevokeAccessResponse

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse
}