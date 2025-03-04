package ge.tkgroup.sharedshift.common.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.tkgroup.sharedshift.common.data.repository.AuthRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.EmployeeRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.EquipmentRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.UserRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.SharedShiftRepositoryImpl
import ge.tkgroup.sharedshift.common.data.repository.WorkDayRepositoryImpl
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.domain.repository.EmployeeRepository
import ge.tkgroup.sharedshift.common.domain.repository.EquipmentRepository
import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.domain.repository.WorkDayRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun provideUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun provideSharedShiftRepository(sharedShiftRepository: SharedShiftRepositoryImpl): SharedShiftRepository

    @Binds
    abstract fun provideEmployeeRepository(employeeRepository: EmployeeRepositoryImpl): EmployeeRepository

    @Binds
    abstract fun provideWorkDaysRepository(workDayRepository: WorkDayRepositoryImpl): WorkDayRepository

    @Binds
    abstract fun provideEquipmentRepository(equipmentRepository: EquipmentRepositoryImpl): EquipmentRepository
}