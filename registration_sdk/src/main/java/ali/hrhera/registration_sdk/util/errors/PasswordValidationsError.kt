package ali.hrhera.registration_sdk.util.errors


data class PasswordValidationsError(val msg: String) : IllegalArgumentException(msg)