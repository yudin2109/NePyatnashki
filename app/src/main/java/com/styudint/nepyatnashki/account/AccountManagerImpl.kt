package com.styudint.nepyatnashki.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException
import javax.inject.Inject

class AccountManagerImpl @Inject constructor() : AccountManager {
    private val authLiveData = MutableLiveData<AuthenticationState>()
    private val listeners = HashSet<AccountManagerListener>()

    override fun currentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun signInWithEmail(email: String, password: String): LiveData<AuthenticationState> {
        authLiveData.postValue(PendingAuthentication)

        val auth = FirebaseAuth.getInstance()
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    authLiveData.postValue(AuthenticationError(it.exception.toString()))
                } else {
                    auth.addAuthStateListener(this)
                }
                notifyAccountChanged()
            }
        } catch (exception: IllegalArgumentException) {
            authLiveData.postValue(AuthenticationError(exception.toString()))
        }

        return authLiveData
    }

    override fun register(email: String, password: String): LiveData<AuthenticationState> {
        authLiveData.postValue(PendingAuthentication)

        val auth = FirebaseAuth.getInstance()
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    authLiveData.postValue(AuthenticationError(it.exception.toString()))
                } else {
                    auth.addAuthStateListener(this)
                }
                notifyAccountChanged()
            }
        } catch (exception: IllegalArgumentException) {
            authLiveData.postValue(AuthenticationError(exception.toString()))
        }
        return authLiveData
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        notifyAccountChanged()
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        if (isAuthenticated()) {
            authLiveData.postValue(SuccessfullyAuthenticated)
        }
    }

    private fun isAuthenticated(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    private fun notifyAccountChanged() {
        listeners.forEach {
            it.accountHasChanged()
        }
    }

    override fun subscribe(listener: AccountManagerListener) {
        listeners.add(listener)
        listener.accountHasChanged()
    }

    override fun unsubscribe(listener: AccountManagerListener) {
        listeners.remove(listener)
    }
}