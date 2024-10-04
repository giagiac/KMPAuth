package it.hypernext.modacenter.fidelity.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val email: String,
    val provider: String,
    val idToken: String,
    val phoneNumber: String,
    val privacy: Boolean,
)