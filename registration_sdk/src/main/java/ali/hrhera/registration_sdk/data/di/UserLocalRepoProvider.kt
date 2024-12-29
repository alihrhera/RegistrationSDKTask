package ali.hrhera.registration_sdk.data.di

import ali.hrhera.registration_sdk.data.local.LocalAppDb
import ali.hrhera.registration_sdk.data.local.UserDao
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserLocalRepoProvider {

    @Provides
    @Singleton
    fun provideDAO(@ApplicationContext context: Context): UserDao = LocalAppDb.userDao(context)


}