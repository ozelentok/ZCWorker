package ozelentok.zcworker

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

internal object AppPreferences {
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun getInt(context: Context, key: String, defaultValue: Int): Int {
        val prefs = getSharedPreferences(context)
        return try {
            prefs.getInt(key, defaultValue)
        } catch (castException: ClassCastException) {
            val value = prefs.getString(key, null) ?: return defaultValue
            value.toInt()
        }
    }

    fun getHost(context: Context): String? {
        return getSharedPreferences(context).getString("host", "")
    }

    fun getPort(context: Context): Int {
        return getInt(context, "port", 4444)
    }
}