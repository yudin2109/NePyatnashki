package com.styudint.nepyatnashki.account

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface AccountManagerListener {
    fun accountHasChanged()
}

sealed class AuthenticationState
object PendingAuthentication : AuthenticationState()
object SuccessfullyAuthenticated : AuthenticationState()
data class AuthenticationError(var error: String) : AuthenticationState()

interface AccountManager : FirebaseAuth.AuthStateListener {
    fun currentUser(): FirebaseUser? // For now next, in the future we will have to have our special wrapping for user
    fun signInWithEmail(email: String, password: String): LiveData<AuthenticationState>
    fun register(email: String, password: String): LiveData<AuthenticationState>
    fun logout()

    fun subscribe(listener: AccountManagerListener)
    fun unsubscribe(listener: AccountManagerListener)
}