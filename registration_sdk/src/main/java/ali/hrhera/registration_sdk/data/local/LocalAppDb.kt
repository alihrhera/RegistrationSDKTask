package ali.hrhera.registration_sdk.data.local

import ali.hrhera.registration_sdk.domain.dto.UserImageEntity
import ali.hrhera.registration_sdk.domain.dto.UserInfoCachingEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * LocalAppDb is the Room Database for the application.
 *
 * It provides a singleton instance of the database and its DAOs.
 */
@Database(
    entities = [UserInfoCachingEntity::class, UserImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalAppDb : RoomDatabase() {
    abstract val userInfoDao: UserDao

    companion object {

        @Volatile
        private var userInfoDao: UserDao? = null


        /**
         * Builds and returns the LocalAppDb instance.
         *
         * @param context The application context.
         * @return The LocalAppDb instance.
         */
        private fun buildDatabase(context: Context): LocalAppDb = Room.databaseBuilder(
            context,
            LocalAppDb::class.java,
            "registerTaskDb"
        ).fallbackToDestructiveMigration()
            .build()


        fun userDao(context: Context): UserDao {
            synchronized(this) {
                if (userInfoDao == null) {
                    userInfoDao = buildDatabase(context.applicationContext).userInfoDao
                }
                return userInfoDao as UserDao
            }
        }


    }
}