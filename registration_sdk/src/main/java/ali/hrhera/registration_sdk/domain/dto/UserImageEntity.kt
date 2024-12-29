package ali.hrhera.registration_sdk.domain.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to hold the image of the user.
 *
 * @property id the id of the image
 * @property userId the id of the user that the image belongs to
 * @property imageData the image data
 */
@Entity(tableName = "userImage")
data class UserImageEntity(
    /**
     * The id of the image.
     */
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    /**
     * The id of the user that the image belongs to.
     */
    val userId: Int,
    /**
     * The image data.
     */
    val imageData: ByteArray
) {

    /**
     * Checks if this object is equal to another object.
     *
     * @param other the object to compare against
     * @return true if the objects are equal, false otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserImageEntity

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return the hash code value
     */
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + userId
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}