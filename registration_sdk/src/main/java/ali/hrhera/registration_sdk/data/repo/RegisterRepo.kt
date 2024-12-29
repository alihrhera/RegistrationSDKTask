package ali.hrhera.registration_sdk.data.repo

import ali.hrhera.registration_sdk.data.local.UserDao
import ali.hrhera.registration_sdk.domain.dto.UserImageEntity
import ali.hrhera.registration_sdk.domain.dto.UserInfoCachingEntity
import ali.hrhera.registration_sdk.domain.mappers.UserEntityMapper
import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.util.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * This class is responsible for providing access to the local database for the user table.
 * It provides a way to insert new user and retrieve user by id.
 */
class RegisterRepo @Inject constructor(
    private val localDb: UserDao,
    private val mapper: UserEntityMapper
) {

    /**
     * Insert a new user into the database.
     *
     * @param user the user to be inserted
     * @return a flow of BaseResponse<User> which is the result of the insertion.
     */
    suspend fun insertNewUser(user: UserInfoCachingEntity): Flow<BaseResponse<User>> {
        return flow {
            try {
                emit(BaseResponse.Loading)
                val id = localDb.insertUser(user)

                val insertedUser = localDb.getUserWithImage(id.toInt())
                insertedUser?.let {
                    emit(BaseResponse.Success(mapper.toModel(insertedUser)))
                } ?: emit(BaseResponse.Error(Throwable("Some Error Error")))
            } catch (e: Throwable) {
                emit(BaseResponse.Error(e))
            }
        }
    }

    /**
     * Save a user image into the database.
     *
     * @param image the image to be saved
     * @param userId the id of the user the image belongs to
     * @return the id of the inserted image
     */
    suspend fun saveUserImage(image: ByteArray, userId: Int): Long =
        localDb.insertImage(UserImageEntity(userId = userId, imageData = image))


    /**
     * Get a user by id from the database.
     *
     * @param id the id of the user to be retrieved
     * @return a flow of BaseResponse<User> which is the result of the retrieval.
     */
    suspend fun getUserById(id: Int): Flow<BaseResponse<User>> {
        return flow {
            try {

                val result = localDb.getUserWithImage(id)
                result?.let {
                    emit(BaseResponse.Success(mapper.toModel(it)))
                } ?: emit(BaseResponse.Error(Throwable("Some Error happened")))
            } catch (e: Throwable) {
                emit(BaseResponse.Error(e))
            }
        }
    }

}