package ali.hrhera.registration_sdk.domain.model

import ali.hrhera.registration_sdk.domain.dto.UserImageEntity
import android.graphics.Bitmap
import androidx.room.Ignore

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val image: List<UserImageEntity> = emptyList(),
    @Ignore
    var bitmaps: List<Bitmap> = emptyList()

)