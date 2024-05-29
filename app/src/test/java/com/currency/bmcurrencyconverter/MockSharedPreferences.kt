package com.currency.bmcurrencyconverter

import android.content.SharedPreferences

class MockSharedPreferences : SharedPreferences {

    private val preferences = mutableMapOf<String, Any>()

    override fun getAll(): Map<String, *> = preferences

    override fun getString(key: String, defValue: String?): String? =
        preferences[key] as? String ?: defValue

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? =
        preferences[key] as? Set<String> ?: defValues

    override fun getInt(key: String, defValue: Int): Int =
        preferences[key] as? Int ?: defValue

    override fun getLong(key: String, defValue: Long): Long =
        preferences[key] as? Long ?: defValue

    override fun getFloat(key: String, defValue: Float): Float =
        preferences[key] as? Float ?: defValue

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        preferences[key] as? Boolean ?: defValue

    override fun contains(key: String): Boolean = preferences.containsKey(key)

    override fun edit(): SharedPreferences.Editor = MockEditor()

    inner class MockEditor : SharedPreferences.Editor {

        override fun putString(key: String, value: String?): SharedPreferences.Editor {
            preferences[key] = value ?: ""
            return this
        }

        override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor {
            preferences[key] = values ?: emptySet<String>()
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            preferences[key] = value
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            preferences[key] = value
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            preferences[key] = value
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            preferences[key] = value
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            preferences.remove(key)
            return this
        }

        override fun clear(): SharedPreferences.Editor {
            preferences.clear()
            return this
        }

        override fun commit(): Boolean = true

        override fun apply() {}
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {}

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {}
}
