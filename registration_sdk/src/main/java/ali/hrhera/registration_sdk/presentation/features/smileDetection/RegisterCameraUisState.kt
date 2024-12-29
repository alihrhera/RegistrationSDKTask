package ali.hrhera.registration_sdk.presentation.features.smileDetection


sealed class RegisterCameraUiState {
    data object Success : RegisterCameraUiState()
    data object Idel : RegisterCameraUiState()
    data object NONE : RegisterCameraUiState()
}

