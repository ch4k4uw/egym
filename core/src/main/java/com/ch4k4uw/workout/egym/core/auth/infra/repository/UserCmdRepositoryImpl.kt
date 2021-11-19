package com.ch4k4uw.workout.egym.core.auth.infra.repository

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserRepository
import com.ch4k4uw.workout.egym.core.auth.infra.injection.FirebaseSubComponent
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserCmdRepositoryImpl @Inject constructor(
    private val dispatchers: AppDispatchers,
    fbSubComponentFactory: FirebaseSubComponent.Factory
) : UserCmdRepository {
    private val fbSubComponent: FirebaseSubComponent = fbSubComponentFactory
        .create()

    private val fbAuth: FirebaseAuth
        get() = fbSubComponent.fbAuth

    override suspend fun findLoggedUser(): Flow<User> {
        return flow {
            val rawUser = fbAuth.currentUser
            val domainUser = if (rawUser == null) {
                User.Empty
            } else {
                User(
                    id = rawUser.uid,
                    name = rawUser.displayName ?: "",
                    email = rawUser.email ?: "",
                    image = rawUser.photoUrl?.toString() ?: ""
                )
            }
            emit(domainUser)
        }.flowOn(dispatchers.io)
    }

    override suspend fun performLogout(): Flow<Unit> {
        return flow {
            fbAuth.signOut()
            emit(Unit)
        }
    }
}