package com.example.kotlinprogectapp.data.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(access: String, refresh: String) {
        prefs.edit {
            putString(KEY_ACCESS, access)
                .putString(KEY_REFRESH, refresh)
        }
    }

    fun saveUserId(id: Long) {
        prefs.edit { putLong(KEY_USER_ID, id) }
    }

    fun getAccessToken(): String?  = prefs.getString(KEY_ACCESS, null)
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)
    fun getCurrentUserId(): Long?  = prefs.getLong(KEY_USER_ID, -1L).takeIf { it != -1L }

    fun clear() = prefs.edit { clear() }

    companion object {
        private const val KEY_ACCESS   = "access_token"
        private const val KEY_REFRESH  = "refresh_token"
        private const val KEY_USER_ID  = "user_id"
    }
}