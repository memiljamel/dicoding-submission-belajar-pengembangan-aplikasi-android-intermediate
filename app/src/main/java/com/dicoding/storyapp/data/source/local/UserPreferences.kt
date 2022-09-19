package com.dicoding.storyapp.data.source.local

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

internal class UserPreferences(context: Context) {

    val spec = KeyGenParameterSpec.Builder(
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
    val masterKey = MasterKey.Builder(context)
        .setKeyGenParameterSpec(spec)
        .build()
    private var pref: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "Session",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setToken(value: String) {
        val editor = pref.edit()
        editor.putString(TOKEN, value)
        editor.apply()
    }

    fun getToken(): String? {
        return pref.getString(TOKEN, "")
    }

    fun removeToken() {
        val editor = pref.edit()
        editor.putString(TOKEN, "")
        editor.apply()
    }

    companion object {
        private const val TOKEN = "token"
    }
}