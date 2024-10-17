package it.hypernext.modacenter.fidelity.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val idToken: String,
    val privacy: Boolean,

    val uid: String,
    val displayName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoURL: String?,
    val isAnonymous: Boolean,
    val isEmailVerified: Boolean,
    val providerId: String
)