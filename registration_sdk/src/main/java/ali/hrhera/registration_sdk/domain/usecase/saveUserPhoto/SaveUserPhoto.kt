package ali.hrhera.registration_sdk.domain.usecase.saveUserPhoto

import ali.hrhera.registration_sdk.data.repo.RegisterRepo
import ali.hrhera.registration_sdk.util.BaseResponse
import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class SaveUserPhoto @Inject constructor(private val repo: RegisterRepo) {

    val response = MutableSharedFlow<BaseResponse<Long>>()

    suspend operator fun invoke(bitmap: Bitmap, userId: Int) {
        val byteArray = bitmap.convertToByteArray()
        val insertedId = repo.saveUserImage(byteArray, userId)
        if (insertedId != 0L) {
            response.emit(BaseResponse.Success(insertedId))
        } else {
            response.emit(BaseResponse.Error(Throwable("Some Error happened")))
        }
    }

    private fun Bitmap.convertToByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
}
