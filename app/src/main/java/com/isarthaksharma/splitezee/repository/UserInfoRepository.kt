package com.isarthaksharma.splitezee.repository

import com.google.firebase.auth.FirebaseAuth
import com.isarthaksharma.splitezee.dataClass.userInfoFromGoogle
import javax.inject.Inject

class UserInfoRepository @Inject constructor(
    private var auth:FirebaseAuth
){
    fun getUserInfo(): userInfoFromGoogle?{
        val user = auth.currentUser
        return user?.providerData?.let {
            userInfoFromGoogle(
                userName = it[0].displayName ?:"",
                userEmail = it[0].email ?:"",
                userProfilePictureUrl = it[0].photoUrl.toString()
            )
        }
    }
}