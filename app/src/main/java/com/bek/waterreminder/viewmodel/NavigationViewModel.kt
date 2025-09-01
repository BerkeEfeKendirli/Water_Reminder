package com.bek.waterreminder.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bek.waterreminder.data.local.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NavigationViewModel(context: Context) : ViewModel() {
  private val _hasSetWeight = MutableStateFlow<Boolean?>(null)
  val hasSetWeight: StateFlow<Boolean?> = _hasSetWeight

  init {
    viewModelScope.launch {
      val hasSetWeightKey = booleanPreferencesKey("has_set_weight")
      val preferences = context.dataStore.data.first()
      _hasSetWeight.value = preferences[hasSetWeightKey] ?: false
    }
  }
}
