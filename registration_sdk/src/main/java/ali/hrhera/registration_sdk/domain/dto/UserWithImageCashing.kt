package ali.hrhera.registration_sdk.domain.dto

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class representing a user with their associated images.
 *
 * @property user The embedded user details.
 * @property userImageCash A list of images associated with the user, nullable if the user has no images.
 */
data class UserWithImageCashing(
    @Embedded val user: UserInfoCachingEntity, // Embeds user details
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val userImageCash: List<UserImageEntity>? // Nullable in case the user doesn't have an image
)