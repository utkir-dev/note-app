package com.example.data.repositories.impl

import android.content.SharedPreferences
import com.example.data.repositories.intrefaces.SharedPrefRepository
import javax.inject.Inject

class SharedPrefRepositoryImpl @Inject constructor(
    val sharedPref: SharedPreferences
) : SharedPrefRepository {
    override suspend fun saveLong(key: String, value: Long) {
        sharedPref.edit().putLong(key, value).apply()
    }

    override suspend fun getLong(key: String): Long {
        return sharedPref.getLong(key, 0)
    }
}