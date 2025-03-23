@file:Suppress("DEPRECATION")

package com.isarthaksharma.splitezee.repository

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {

    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(0))
            .requestEmail()
            .requestProfile()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    suspend fun signInWithGoogle(task: Task<GoogleSignInAccount>): AuthResponse {
        return try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            saveUserToFireStore(authResult.user)
            AuthResponse.Success
        } catch (e: Exception) {
            AuthResponse.Error(e.message ?: "Unknown error")
        }
    }

    // Uploading to FireStore
    private suspend fun saveUserToFireStore(user: FirebaseUser?) {
        user?.let {
            val userData = hashMapOf(
                "userId" to it.uid,
                "email" to it.email,
                "name" to it.displayName,
                "profilePic" to it.photoUrl.toString()
            )
            firestore.collection("users").document(it.uid).set(userData).await()
        }
    }

    fun logout() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
