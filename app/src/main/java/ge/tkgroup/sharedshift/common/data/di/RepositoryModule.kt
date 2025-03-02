package ge.tkgroup.sharedshift.common.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.tkgroup.sharedshift.common.data.repository.AuthRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.EmployeeRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.UserRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.SharedShiftRepositoryImpl
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.domain.repository.EmployeeRepository
import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun provideSharedShiftRepository(sharedShiftRepository: SharedShiftRepositoryImpl): SharedShiftRepository

    @Binds
    abstract fun provideEmployeeRepository(employeeRepository: EmployeeRepositoryImpl): EmployeeRepository
}