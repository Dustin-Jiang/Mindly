package top.tsukino.llmdemo.config

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore("settings")

class LLMPreferences(context: Context) {
    private val store = context.dataStore

    private val ENABLE_SUMMARY_TITLE_KEY = booleanPreferencesKey("enable_summary_title")
    val enableSummaryTitle = PreferencesDataStoreItem<Boolean>(ENABLE_SUMMARY_TITLE_KEY, false, store)

    private val TASK_MODEL_ID = stringPreferencesKey("task_model_id")
    val taskModelId = PreferencesDataStoreItem<String>(TASK_MODEL_ID, "", store)
}