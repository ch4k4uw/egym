package com.ch4k4uw.workout.egym.core.auth.infra.injection

import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserRepository
import com.ch4k4uw.workout.egym.core.auth.infra.repository.UserCmdRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserRepository(impl: UserCmdRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserCmdRepository(impl: UserCmdRepositoryImpl): UserCmdRepository
}