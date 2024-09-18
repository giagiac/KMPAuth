package it.hypernext.modacenter.fidelity

import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider


object AppInitializer {
    fun onApplicationStart() {
        onApplicationStartPlatformSpecific()
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = "482654189035-bp4j3ocmtim7tfob8benp5uqpacvc59u.apps.googleusercontent.com"))
    }
}