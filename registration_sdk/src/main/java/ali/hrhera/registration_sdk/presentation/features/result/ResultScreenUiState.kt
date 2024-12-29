package ali.hrhera.registration_sdk.presentation.features.result

import ali.hrhera.registration_sdk.domain.model.User

sealed class ResultScreenUiState {
    data object Loading : ResultScreenUiState()
    data class Error(val message: String?) : ResultScreenUiState()
    data class Success(val user: User) : ResultScreenUiState()
    data object Idel : ResultScreenUiState()
}