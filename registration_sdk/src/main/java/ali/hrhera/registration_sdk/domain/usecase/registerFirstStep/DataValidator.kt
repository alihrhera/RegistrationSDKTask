package ali.hrhera.registration_sdk.domain.usecase.registerFirstStep

import ali.hrhera.registration_sdk.domain.dto.UserInfoCachingEntity
import ali.hrhera.registration_sdk.domain.mappers.UserEntityMapper
import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.util.errors.EmailValidationError
import ali.hrhera.registration_sdk.util.errors.NameValidationError
import ali.hrhera.registration_sdk.util.errors.PasswordValidationsError
import ali.hrhera.registration_sdk.util.errors.PhoneValidationError
import javax.inject.Inject

class DataValidator @Inject constructor(private val mapper: UserEntityMapper) {


    // ? some business logic to validate data
    fun validated(user: User): UserInfoCachingEntity {

        if (user.name.isBlank() || user.name.length < 3) {
            throw NameValidationError("Name is too short")
        }
        user.phone.isValidPhoneNumber()
        user.email.isValidEmailAddress()
        user.password.isValidPassword()

        return mapper.toEntity(user).user
    }


    private fun String?.isValidEmailAddress() {
        if (this.isNullOrBlank()) throw EmailValidationError("email is not valid")
        val emailRegex = """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""".toRegex()
        if (!emailRegex.matches(this)) {
            throw EmailValidationError("email is not valid")
        }
    }


    private fun String?.isValidPhoneNumber() {
        if (this.isNullOrBlank()) throw PhoneValidationError("phone is not valid")
        val phoneRegex = """^01[0-9]{9}$""".toRegex()
        if (!phoneRegex.matches(this)) throw PhoneValidationError("phone is not valid")
    }

    private fun String?.isValidPassword() {
        if (this.isNullOrBlank()) throw PasswordValidationsError("Password is not valid or too short")
        val passwordRegex = """^(?=.*[A-Za-z\d])[A-Za-z\d]{6,}$""".toRegex()
        if (!passwordRegex.matches(this)) throw PasswordValidationsError("Password is not valid or too short")
    }


}