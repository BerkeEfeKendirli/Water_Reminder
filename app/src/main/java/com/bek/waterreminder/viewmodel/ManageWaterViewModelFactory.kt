package com.bek.waterreminder.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManageWaterViewModelFactory(private val dataStore: DataStore<Preferences>) :
    ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ManageWaterViewModel::class.java)) {
      return ManageWaterViewModel(dataStore) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
