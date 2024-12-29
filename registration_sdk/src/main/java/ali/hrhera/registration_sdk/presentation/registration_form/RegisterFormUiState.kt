package ali.hrhera.registration_sdk.presentation.registration_form


data class RegisterFormUiState(
    val generalError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val done: Boolean = false,
    val insertedUserId: Int = 0
)