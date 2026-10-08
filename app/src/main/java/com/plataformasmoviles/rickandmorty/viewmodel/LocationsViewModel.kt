package com.plataformasmoviles.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plataformasmoviles.rickandmorty.data.LocationDb
import com.plataformasmoviles.rickandmorty.model.Location
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadingJob?.cancel()
        _uiState.value = LocationsUiState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(4000)

            _uiState.value = LocationsUiState(
                isLoading = false,
                data = locationDb.getAllLocations(),
                hasError = false
            )
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