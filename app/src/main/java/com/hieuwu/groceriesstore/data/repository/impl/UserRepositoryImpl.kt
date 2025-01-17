package com.hieuwu.groceriesstore.data.repository.impl

import com.hieuwu.groceriesstore.data.database.dao.UserDao
import com.hieuwu.groceriesstore.data.database.entities.User
import com.hieuwu.groceriesstore.data.database.entities.asDomainModel
import com.hieuwu.groceriesstore.data.network.dto.UserDto
import com.hieuwu.groceriesstore.data.repository.UserRepository
import com.hieuwu.groceriesstore.utilities.CollectionNames
import com.hieuwu.groceriesstore.utilities.SupabaseMapper
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import java.util.*
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authService: GoTrue,
    private val postgrest: Postgrest,
) : UserRepository {

    override suspend fun createAccount(email: String, password: String, name: String): Boolean {
        return try {
            authService.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val userDto = UserDto(
                id = UUID.randomUUID().toString(),
                name = name,
                email = email,
                address = null,
                phone = null,
                isOrderCreatedNotiEnabled = false,
                isPromotionNotiEnabled = false,
                isDataRefreshedNotiEnabled = false
            )
            postgrest[CollectionNames.users].insert(value = userDto, upsert = true)
            val user = SupabaseMapper.mapDtoToEntity(userDto)
            userDao.insert(user)
            true
        } catch (e: Exception) {
            Timber.e(e.message)
            false
        }
    }

    override suspend fun authenticate(email: String, password: String): Boolean {
        return try {
            authService.loginWith(Email) {
                this.email = email
                this.password = password
            }

            val userDto = postgrest[CollectionNames.users].select().decodeSingle<UserDto>()
            val user = SupabaseMapper.mapDtoToEntity(userDto)
            userDao.insert(user)
            true
        } catch (e: Exception) {
            Timber.e(e.message)
            false
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        name: String,
        email: String,
        phone: String,
        address: String
    ) {
        val dbUser = User(
            userId, name, email, address, phone,
            isOrderCreatedNotiEnabled = false,
            isPromotionNotiEnabled = false,
            isDataRefreshedNotiEnabled = false
        )
        try {
            postgrest[CollectionNames.users].update(
                {
                    UserDto::phone setTo phone
                    UserDto::email setTo email
                    UserDto::address setTo address
                }
            ) {
                UserDto::id eq userId
            }
            userDao.insert(dbUser)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun clearUser() {
        userDao.clear()
    }

    override suspend fun updateUserSettings(
        id: String,
        isOrderCreatedEnabled: Boolean,
        isDatabaseRefreshedEnabled: Boolean,
        isPromotionEnabled: Boolean
    ) {
        try {
            postgrest[CollectionNames.users].update(
                {
                    UserDto::isOrderCreatedNotiEnabled setTo isOrderCreatedEnabled
                    UserDto::isDataRefreshedNotiEnabled setTo isDatabaseRefreshedEnabled
                    UserDto::isPromotionNotiEnabled setTo isPromotionEnabled
                }
            ) {
                UserDto::id eq id
            }
            userDao.updateUserSettings(
                id,
                isOrderCreatedEnabled,
                isDatabaseRefreshedEnabled,
                isPromotionEnabled
            )
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    override fun getCurrentUser() = userDao.getCurrentUser().map {
        it?.asDomainModel()
    }

}
