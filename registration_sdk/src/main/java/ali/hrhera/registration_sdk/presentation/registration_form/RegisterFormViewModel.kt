package ali.hrhera.registration_sdk.presentation.registration_form

import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.domain.usecase.registerFirstStep.RegisterStepOneUseCase
import ali.hrhera.registration_sdk.presentation.registration_form.RegisterFormUiState
import ali.hrhera.registration_sdk.util.BaseResponse
import ali.hrhera.registration_sdk.util.errors.EmailValidationError
import ali.hrhera.registration_sdk.util.errors.NameValidationError
import ali.hrhera.registration_sdk.util.errors.PasswordValidationsError
import ali.hrhera.registration_sdk.util.errors.PhoneValidationError
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterFormViewModel @Inject constructor(
    private val registerStepOneUseCase: RegisterStepOneUseCase
) : ViewModel() {


    val registerScreenState = mutableStateOf(RegisterFormUiState())

    init {


        viewModelScope.launch {
            registerStepOneUseCase.response.collectLatest {
                when (it) {
                    is BaseResponse.Success -> {
                        registerScreenState.value = RegisterFormUiState(done = true, insertedUserId = it.data.id)
                    }

                    is BaseResponse.Error -> errorHandler(it.throwable)

                    is BaseResponse.Loading -> registerScreenState.value = RegisterFormUiState(isLoading = true)

                }
            }
        }
    }


    private fun errorHandler(error: Throwable?) {
        when (error) {
            is NameValidationError -> {
                registerScreenState.value = RegisterFormUiState(nameError = error.message)
            }

            is PhoneValidationError -> {
                registerScreenState.value = RegisterFormUiState(phoneError = error.message)
            }

            is PasswordValidationsError -> {
                registerScreenState.value = RegisterFormUiState(passwordError = error.message)
            }

            is EmailValidationError -> {
                registerScreenState.value = RegisterFormUiState(emailError = error.message)
            }

            else -> {
                registerScreenState.value = RegisterFormUiState(generalError = error?.message ?: "")

            }
        }
    }

    fun registerUser(name: String?, phone: String?, email: String?, password: String?) {

        viewModelScope.launch {
            registerStepOneUseCase(
                User(
                    name = name ?: "",
                    phone = phone ?: "",
                    email = email ?: "",
                    password = password ?: ""
                )
            )
        }

    }


}