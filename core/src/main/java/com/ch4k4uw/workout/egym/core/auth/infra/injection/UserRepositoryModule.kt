package com.ch4k4uw.workout.egym.core.auth.infra.injection

import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserRepository
import com.ch4k4uw.workout.egym.core.auth.infra.repository.UserCmdRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindUserRepository(impl: UserCmdRepositoryImpl): UserRepository

    @Binds
    @ViewModelScoped
    abstract fun bindUserCmdRepository(impl: UserCmdRepositoryImpl): UserCmdRepository
}