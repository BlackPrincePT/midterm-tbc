package ge.tkgroup.sharedshift.common.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.tkgroup.sharedshift.common.data.repository.AuthRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.SharedShiftRepositoryImpl
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun provideSharedShiftRepository(sharedShiftRepository: SharedShiftRepositoryImpl): SharedShiftRepository
}