package com.ch4k4uw.workout.egym.core.auth.infra.repository

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserRepository
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fbAuth: FirebaseAuth,
    private val dispatchers: AppDispatchers
) : UserRepository {
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
}