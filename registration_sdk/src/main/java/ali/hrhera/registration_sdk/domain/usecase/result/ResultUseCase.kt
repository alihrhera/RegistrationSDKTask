package ali.hrhera.registration_sdk.domain.usecase.result

import ali.hrhera.registration_sdk.data.repo.RegisterRepo
import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.util.BaseResponse
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ResultUseCase @Inject constructor(
    private val repo: RegisterRepo,
) {

    val response = MutableSharedFlow<BaseResponse<User>>()
    suspend operator fun invoke(id: Int) {
        repo.getUserById(id).collectLatest { responseValue ->
            if (responseValue is BaseResponse.Success) {
                val bitmap = responseValue.data.image.map { it.imageData }.toBitmap()
                response.emit(BaseResponse.Success(responseValue.data.copy(bitmaps = bitmap)))
            } else response.emit(responseValue)
        }
    }

    private fun List<ByteArray>.toBitmap(): List<Bitmap> {
        return this.map { it.mapToBitmap() }
    }

    private fun ByteArray.mapToBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
}