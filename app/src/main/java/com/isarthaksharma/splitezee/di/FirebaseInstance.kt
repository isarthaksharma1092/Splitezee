package com.isarthaksharma.splitezee.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Dagger Hilt module to provide dependencies related to Firebase Authentication and Credential Management
@Module
@InstallIn(SingletonComponent::class)
class FirebaseInstance {

    // Provides a singleton instance of Firebase Authentication
    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth = Firebase.auth

    // Provides a singleton instance of CredentialManager for handling user credentials securely
    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager{
        return CredentialManager.create(context)
    }

}
