package ali.hrhera.registration_sdk.domain.usecase.registerFirstStep

import ali.hrhera.registration_sdk.data.repo.RegisterRepo
import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.util.BaseResponse
import ali.hrhera.registration_sdk.util.errors.EmailValidationError
import ali.hrhera.registration_sdk.util.errors.PhoneValidationError
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RegisterStepOneUseCase @Inject constructor(
    private val mainRepo: RegisterRepo, private val validator: DataValidator
) {

    var response = MutableSharedFlow<BaseResponse<User>>()
    suspend operator fun invoke(user: User) {
        try {
            response.emit(BaseResponse.Loading)
            val validated = validator.validated(user)
            val result = mainRepo.insertNewUser(validated)
            responseCheck(result)
        } catch (e: Throwable) {
            response.emit(BaseResponse.Error(e))
        }
    }

    private suspend fun RegisterStepOneUseCase.responseCheck(result: Flow<BaseResponse<User>>) {
        result.collectLatest {
            if (it is BaseResponse.Error) {
                Log.w("TAG", "responseCheck: ${it.throwable}")
                errorHandel(it)
            } else {
                response.emit(it)
            }
        }
    }

    private suspend fun errorHandel(it: BaseResponse.Error<User>) {
        if (it.throwable is SQLiteConstraintException) {
            if (it.throwable.message?.contains("email") == true)
                response.emit(BaseResponse.Error(EmailValidationError("Email already exists")))
            if (it.throwable.message?.contains("phone") == true)
                response.emit(BaseResponse.Error(PhoneValidationError("Phone already exists")))
        } else response.emit(it)
    }


}

