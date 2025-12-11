package com.example.androidproject

import android.content.Context
import com.google.gson.Gson

object NotificationStorage {

    private const val PREF_NAME = "notifications"
    private const val KEY_LIST = "notification_list"

    fun save(context: Context, item: NotificationItem) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val list = getAll(context).toMutableList()
        list.add(0, item)
        prefs.edit().putString(KEY_LIST, Gson().toJson(list)).apply()
    }

    fun getAll(context: Context): List<NotificationItem> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_LIST, null) ?: return emptyList()
        return Gson().fromJson(json, Array<NotificationItem>::class.java).toList()
    }

    fun clearAll(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().remove(KEY_LIST).apply()
    }
}
