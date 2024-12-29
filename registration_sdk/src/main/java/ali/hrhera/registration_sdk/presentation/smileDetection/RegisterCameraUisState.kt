package ali.hrhera.registration_sdk.presentation.smileDetection


sealed class RegisterCameraUiState {
    data object Success : RegisterCameraUiState()
    data object Idel : RegisterCameraUiState()
}

