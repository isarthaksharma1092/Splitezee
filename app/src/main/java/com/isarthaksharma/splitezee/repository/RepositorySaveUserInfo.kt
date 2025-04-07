package com.isarthaksharma.splitezee.repository

import com.isarthaksharma.splitezee.localStorage.dao.DaoSavedUserInfo
import com.isarthaksharma.splitezee.localStorage.dataClass.SavedUserInfoDataClass
import javax.inject.Inject

class RepositorySaveUserInfo @Inject constructor(
    private val daoSavedUserInfo: DaoSavedUserInfo
) {

    suspend fun checkUserInfoExist(email: String): Boolean {
        return daoSavedUserInfo.isUserEmailExists(email)
    }

    suspend fun insertUser(savedUserInfoDataClass: SavedUserInfoDataClass) {
        daoSavedUserInfo.insertUserInfo(savedUserInfoDataClass)
    }

    suspend fun getUser(email: String): SavedUserInfoDataClass {
        return daoSavedUserInfo.getUserInfoByEmail(email)
    }
}