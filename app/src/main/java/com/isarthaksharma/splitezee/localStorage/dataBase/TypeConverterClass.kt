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
    fun fromStringList(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromMap(value: Map<String, Double>): String = Gson().toJson(value)

    @TypeConverter
    fun toMap(value: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(value, type)
    }
}
