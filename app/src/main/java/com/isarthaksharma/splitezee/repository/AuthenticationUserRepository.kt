package com.isarthaksharma.splitezee.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.isarthaksharma.splitezee.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthenticationUserRepository @Inject constructor(
    private var auth: FirebaseAuth,
    @ApplicationContext private var context: Context
) {
    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.Web_ClintId))
            .setAutoSelectEnabled(false)  // Force showing account selection screen
            .setFilterByAuthorizedAccounts(false)
            .setNonce(nonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    user?.reload()?.addOnCompleteListener {
                                        val updatedUser = FirebaseAuth.getInstance().currentUser
                                        var finalName = updatedUser?.displayName

                                        if (finalName.isNullOrEmpty()) {
                                            val signInAccount = GoogleSignIn.getLastSignedInAccount(context)
                                            finalName = signInAccount?.displayName
                                        }

                                        if (finalName.isNullOrEmpty()) {
                                            val profileUpdates = userProfileChangeRequest {
                                                displayName = "Default User"
                                            }
                                            updatedUser?.updateProfile(profileUpdates)
                                        }

                                        Log.d("FirebaseUser", "Final Name: $finalName")
                                    }

                                    trySend(AuthResponse.Success)
                                } else {
                                    trySend(AuthResponse.Error(task.exception?.message ?: " "))
                                }
                            }
                    } catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(message = e.message ?: ""))
                    }
                }
            }
        } catch (e: Exception) {
            trySend(AuthResponse.Error(message = e.message ?: ""))
        }
        awaitClose()
    }

    fun logout(){
        return auth.signOut()
    }

    fun currentUser(): FirebaseUser?{
        return auth.currentUser
    }


    private fun nonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val msgDigest = MessageDigest.getInstance("SHA-256")
        val digest = msgDigest.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%20x".format(it)
        }
    }

}


