package it.hypernext.modacenter.fidelity.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import it.hypernext.modacenter.fidelity.domain.User

@Dao
interface UserDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Transaction
    @Query("SELECT * FROM user WHERE _id = :userId")
    suspend fun getUserById(userId: Int): User

    @Transaction
    @Query("DELETE FROM user WHERE _id = :userId")
    suspend fun deleteUserById(userId: Int)
}