package com.plataformasmoviles.rickandmorty.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.toModel
import com.plataformasmoviles.rickandmorty.model.Location
import com.plataformasmoviles.rickandmorty.navigation.LocationDetailDestination
import kotlinx.coroutines.Job
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
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val locationDao =
        AppDatabase.getDatabase(application).locationDao()

    private val locationId =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val _uiState =
        MutableStateFlow(LocationDetailUiState())

    val uiState: StateFlow<LocationDetailUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()

        _uiState.value = LocationDetailUiState(
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            try {
                locationDao.getLocationById(locationId)
                    .collect { entity ->

                        _uiState.value = LocationDetailUiState(
                            isLoading = false,
                            data = entity?.toModel(),
                            hasError = entity == null
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = LocationDetailUiState(
                    isLoading = false,
                    hasError = true
                )
            }
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
