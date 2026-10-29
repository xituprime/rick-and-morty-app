package com.plataformasmoviles.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.toModel
import com.plataformasmoviles.rickandmorty.model.Location
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val locationDao =
        AppDatabase.getDatabase(application).locationDao()

    private val _uiState = MutableStateFlow(LocationsUiState())

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadingJob?.cancel()

        _uiState.value = LocationsUiState(
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            try {
                locationDao.getAllLocations().collect { entities ->

                    _uiState.value = LocationsUiState(
                        isLoading = false,
                        data = entities.map { it.toModel() },
                        hasError = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = LocationsUiState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun simulateError() {
        if (!_uiState.value.isLoading) return

        loadingJob?.cancel()

        _uiState.value = LocationsUiState(
            isLoading = false,
            hasError = true
        )
    }
}
