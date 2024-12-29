package ali.hrhera.registration_sdk.domain.mappers

import ali.hrhera.registration_sdk.domain.dto.UserInfoCachingEntity
import ali.hrhera.registration_sdk.domain.dto.UserWithImageCashing
import ali.hrhera.registration_sdk.domain.model.User
import ali.hrhera.registration_sdk.util.MainMapper
import javax.inject.Inject

/**
 * This class is a mapper that maps between the [User] and [UserWithImageCashing]
 * It is used to map the user data from the domain model to the data model and vice versa
 *
 */
class UserEntityMapper @Inject constructor() : MainMapper<UserWithImageCashing, User> {

    /**
     * Maps the [UserWithImageCashing] to [User]
     * @param input the input [UserWithImageCashing]
     * @return the mapped [User]
     */
    override fun toModel(input: UserWithImageCashing): User {
        return User(
            id = input.user.id,
            name = input.user.name,
            email = input.user.email,
            phone = input.user.phoneNumber,
            password = input.user.password,
            image = input.userImageCash ?: emptyList(),
        )

    }

    /**
     * Maps the [User] to [UserWithImageCashing]
     * @param input the input [User]
     * @return the mapped [UserWithImageCashing]
     */
    override fun toEntity(input: User): UserWithImageCashing {
        return UserWithImageCashing(
            user = UserInfoCachingEntity(
                name = input.name,
                email = input.email,
                phoneNumber = input.phone,
                password = input.password,
            ),
            userImageCash = input.image
        )
    }


}