package ali.hrhera.registration_sdk.util

sealed class BaseResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error<out T : Any>(val throwable: Throwable) : BaseResponse<T>()
    data object Loading : BaseResponse<Nothing>()
}