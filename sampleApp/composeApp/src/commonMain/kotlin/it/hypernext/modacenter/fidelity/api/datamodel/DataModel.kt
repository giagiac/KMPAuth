package it.hypernext.modacenter.fidelity.api.datamodel

import kotlinx.serialization.Serializable

@Serializable
data class CensoredText(val result: String)

@Serializable
data class DataUser(val token: String)