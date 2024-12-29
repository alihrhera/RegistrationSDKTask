package ali.hrhera.registration_sdk.data.local

import ali.hrhera.registration_sdk.domain.dto.UserImageEntity
import ali.hrhera.registration_sdk.domain.dto.UserInfoCachingEntity
import ali.hrhera.registration_sdk.domain.dto.UserWithImageCashing
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserInfoCachingEntity): Long // Returns the user ID

    @Insert
    suspend fun insertImage(userImageCash: UserImageEntity): Long


    @Transaction
    @Query("SELECT * FROM userInfo WHERE id = :userId")
    suspend fun getUserWithImage(userId: Int): UserWithImageCashing?


}