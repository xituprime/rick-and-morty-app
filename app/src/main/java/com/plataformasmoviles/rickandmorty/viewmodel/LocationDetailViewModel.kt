package com.plataformasmoviles.rickandmorty.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.data.LocationDb
import com.plataformasmoviles.rickandmorty.model.Location
import com.plataformasmoviles.rickandmorty.navigation.LocationDetailDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationId =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(LocationDetailUiState())
    val uiState: StateFlow<LocationDetailUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()
        _uiState.value = LocationDetailUiState(isLoading = true)

        loadingJob = viewModelScope.launch {
            delay(2000)

            val location = runCatching {
                locationDb.getLocationById(locationId)
            }.getOrNull()

            _uiState.value = LocationDetailUiState(
                isLoading = false,
                data = location,
                hasError = location == null
            )
        }
    }

    fun simulateError() {
        if (!_uiState.value.isLoading) return

        loadingJob?.cancel()

        _uiState.value = LocationDetailUiState(
            isLoading = false,
            hasError = true
        )
    }
}