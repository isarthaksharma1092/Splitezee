package com.isarthaksharma.splitezee.localStorage.dataBase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass

class TypeConverterClass {

    private val gson = Gson()

    @TypeConverter
    fun fromGroupMemberList(value: List<GroupMemberDataClass>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toGroupMemberList(value: String): List<GroupMemberDataClass> {
        val type = object : TypeToken<List<GroupMemberDataClass>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromGroupExpenseList(value: List<GroupExpenseDataClass>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toGroupExpenseList(value: String): List<GroupExpenseDataClass> {
        val type = object : TypeToken<List<GroupExpenseDataClass>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}
